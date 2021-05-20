package market.serviceImpl;

import market.dto.mapper.CustomerOrderMapper;
import market.dto.model.customerOrder.CustomerOrderDto;
import market.dto.model.customerOrder.CustomerOrderReadDto;
import market.exception.CustomException;
import market.model.customerOrder.CustomerOrder;
import market.model.customerOrder.CustomerOrderStatus;
import market.model.orderDetails.OrderDetails;
import market.model.payment.Payment;
import market.model.payment.PaymentStatus;
import market.model.user.User;
import market.repository.CustomerOrderRepository;
import market.repository.OrderDetailsRepository;
import market.repository.PaymentRepository;
import market.repository.UserRepository;
import market.service.CustomerOrderService;
import market.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CustomerOrderServiceImpl implements CustomerOrderService {

  @Autowired
  private CustomerOrderRepository customerOrderRepository;

  @Autowired
  private OrderDetailsRepository orderDetailsRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private ProductService productService;

  /**
   * Creates a customerOrder
   */
  @Override
  public CustomerOrderReadDto create(CustomerOrderDto entry) {
    Optional<User> user = userRepository.findById(entry.getUserId());

    if (!user.isPresent()) {
      throw new CustomException("The user does not exist.", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    CustomerOrder customerOrder = new CustomerOrder()
            .setDate(LocalDate.now())
            .setAmount(0)
            .setStatus(CustomerOrderStatus.CREATED)
            .setUser(user.get());

    customerOrder = customerOrderRepository.save(customerOrder);

    return CustomerOrderMapper.toCustomerOrderDto(customerOrder);
  }

  /**
   * Finds a customerOrder by his id
   */
  @Override
  public CustomerOrderReadDto findById(Long id) {
    Optional<CustomerOrder> customerOrder = customerOrderRepository.findById(id);

    return customerOrder.map(CustomerOrderMapper::toCustomerOrderDto).orElse(null);
  }

  /**
   * Returns all customerOrders
   */
  @Override
  public List<CustomerOrderReadDto> findAll() {
    List<CustomerOrder> allCustomerOrders = customerOrderRepository.findAll();

    return allCustomerOrders.stream()
            .map(CustomerOrderMapper::toCustomerOrderDto)
            .collect(Collectors.toList());
  }

  @Override
  public CustomerOrderReadDto findUserOrderById(Long userId, Long id) {
    Optional<CustomerOrder> customerOrder = customerOrderRepository.findCustomerOrderById(userId, id);

    return customerOrder.map(CustomerOrderMapper::toCustomerOrderDto).orElse(null);
  }

  @Override
  public List<CustomerOrderReadDto> findAllByUser(Long userId) {
    List<CustomerOrder> allCustomerOrders = customerOrderRepository.findAllByUser(userId);

    return allCustomerOrders.stream()
            .map(CustomerOrderMapper::toCustomerOrderDto)
            .collect(Collectors.toList());
  }

  @Override
  public void recalculateAmountById(Long id) {
    Optional<CustomerOrder> customerOrder = customerOrderRepository.findById(id);

    if (!customerOrder.isPresent()) {
      throw new CustomException("The order does not exist.", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    CustomerOrder customerOrderUpdated = customerOrder.get();
    List<OrderDetails> orderDetailsList = orderDetailsRepository.findAllByCustomerOrderId(id);

    double amount = orderDetailsList.stream().mapToDouble(x -> x.getQuantity() * x.getProduct().getPrice()).sum();
    customerOrderUpdated.setAmount(amount);
    customerOrderRepository.save(customerOrderUpdated);
  }

  @Override
  public CustomerOrderReadDto checkout(Long id, Long paymentId) {
    Optional<CustomerOrder> customerOrder = customerOrderRepository.findById(id);

    if (!customerOrder.isPresent()) {
      throw new CustomException("The order does not exist.", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    Optional<Payment> payment = paymentRepository.findById(id);

    if (!payment.isPresent()) {
      throw new CustomException("The payment does not exist.", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    List<OrderDetails> orderDetailsList = orderDetailsRepository.findAllByCustomerOrderId(customerOrder.get().getId());

    for (OrderDetails orderDetails : orderDetailsList) {
      if (!productService.hasAvailableStock(orderDetails.getProduct().getId(), orderDetails.getQuantity())) {
        throw new CustomException("Product out of stock. Please remove the product and make the payment again.", HttpStatus.PRECONDITION_FAILED);
      }
    }

    // Customer Order Processing

    CustomerOrder updatedCustomerOrder = customerOrder.get()
            .setStatus(CustomerOrderStatus.PAID);

    customerOrderRepository.save(updatedCustomerOrder);

    // Payment Processing

    Payment updatedPayment = payment.get()
            .setStatus(PaymentStatus.AUTHORIZED);

    paymentRepository.save(updatedPayment);

    return CustomerOrderMapper.toCustomerOrderDto(updatedCustomerOrder);
  }
}