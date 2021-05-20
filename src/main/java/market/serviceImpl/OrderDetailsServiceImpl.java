package market.serviceImpl;

import market.dto.mapper.OrderDetailsMapper;
import market.dto.model.orderDetails.OrderDetailsDto;
import market.dto.model.orderDetails.OrderDetailsReadDto;
import market.exception.CustomException;
import market.model.customerOrder.CustomerOrder;
import market.model.orderDetails.OrderDetails;
import market.model.product.Product;
import market.repository.CustomerOrderRepository;
import market.repository.OrderDetailsRepository;
import market.repository.ProductRepository;
import market.service.CustomerOrderService;
import market.service.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {

  @Autowired
  private OrderDetailsRepository orderDetailsRepository;

  @Autowired
  private CustomerOrderRepository customerOrderRepository;

  @Autowired
  private CustomerOrderService customerOrderService;

  @Autowired
  private ProductRepository productRepository;

  /**
   * Creates a orderDetails
   */
  @Override
  public OrderDetailsReadDto create(OrderDetailsDto entry) {
    Optional<Product> product = productRepository.findById(entry.getProductId());

    if (!product.isPresent()) {
      throw new CustomException("The product does not exist.", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    Optional<CustomerOrder> customerOrder = customerOrderRepository.findById(entry.getCustomerOrderId());

    if (!customerOrder.isPresent()) {
      throw new CustomException("The order does not exist.", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    OrderDetails orderDetails = new OrderDetails()
            .setQuantity(entry.getQuantity())
            .setProduct(product.get())
            .setCustomerOrder(customerOrder.get());

    orderDetails = orderDetailsRepository.save(orderDetails);
    customerOrderService.recalculateAmountById(orderDetails.getCustomerOrder().getId());

    return OrderDetailsMapper.toOrderDetailsDto(orderDetails);
  }

  /**
   * Finds a orderDetails by his id
   */
  @Override
  public OrderDetailsReadDto findById(Long id) {
    Optional<OrderDetails> orderDetails = orderDetailsRepository.findById(id);

    return orderDetails.map(OrderDetailsMapper::toOrderDetailsDto).orElse(null);
  }

  /**
   * Returns all orderDetails
   */
  @Override
  public List<OrderDetailsReadDto> findAll() {
    List<OrderDetails> allOrderDetails = orderDetailsRepository.findAll();

    return allOrderDetails.stream()
            .map(OrderDetailsMapper::toOrderDetailsDto)
            .collect(Collectors.toList());
  }

  /**
   * Returns all orderDetails of a specific user
   * @return
   */
  @Override
  public OrderDetailsReadDto findUserOrderDetailsById(Long userId, Long id) {
    Optional<OrderDetails> orderDetails = orderDetailsRepository.findOrderDetailsById(userId, id);

    if (!orderDetails.isPresent()) {
      throw new CustomException("The order details does not exist.", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    return OrderDetailsMapper.toOrderDetailsDto(orderDetails.get());
  }

  @Override
  public List<OrderDetailsReadDto> findAllByUser(Long userId) {
    List<OrderDetails> allOrderDetails = orderDetailsRepository.findAllByUser(userId);

    return allOrderDetails.stream()
            .map(OrderDetailsMapper::toOrderDetailsDto)
            .collect(Collectors.toList());
  }

  /**
   * Updates a product
   */
  @Override
  public OrderDetailsReadDto update(OrderDetailsDto entry, Long userId, boolean byPass) {
    Optional<OrderDetails> orderDetails = orderDetailsRepository.findById(entry.getId());

    if (!orderDetails.isPresent()) {
      throw new CustomException("The orderDetails does not exist.", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    OrderDetails orderDetailsUpdated = orderDetails.get();
    Optional<CustomerOrder> customerOrder = customerOrderRepository.findById(orderDetailsUpdated.getCustomerOrder().getId());

    if (!customerOrder.isPresent()) {
      throw new CustomException("The order does not exist.", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    if (!byPass && !customerOrder.get().getUser().getId().equals(userId)) {
      throw new CustomException("You are not the owner of this order.", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    if (entry.getQuantity() > 0) {
      orderDetailsUpdated.setQuantity(entry.getQuantity());
    }

    orderDetailsUpdated = orderDetailsRepository.save(orderDetailsUpdated);
    customerOrderService.recalculateAmountById(orderDetailsUpdated.getCustomerOrder().getId());

    return OrderDetailsMapper.toOrderDetailsDto(orderDetailsUpdated);
  }
}