package market.serviceImpl;

import market.dto.mapper.PaymentMapper;
import market.dto.model.payment.PaymentDto;
import market.dto.model.payment.PaymentReadDto;
import market.exception.CustomException;
import market.model.customerOrder.CustomerOrder;
import market.model.payment.Payment;
import market.model.payment.PaymentStatus;
import market.model.payment.PaymentType;
import market.repository.CustomerOrderRepository;
import market.repository.PaymentRepository;
import market.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class PaymentServiceImpl implements PaymentService {

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private CustomerOrderRepository customerOrderRepository;

  /**
   * Creates a payment
   */
  @Override
  public PaymentReadDto create(PaymentDto entry, Long customerOrderId) {
    Optional<CustomerOrder> customerOrder = customerOrderRepository.findById(customerOrderId);

    if (!customerOrder.isPresent()) {
      throw new CustomException("The order does not exist.", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    if (customerOrder.get().getAmount() > entry.getAmount()) {
      throw new CustomException(String.format("It is not possible to make the payment because the amount sent ($%s) is less than the amount of the service ($%s).",
              entry.getAmount(), customerOrder.get().getAmount()), HttpStatus.PRECONDITION_FAILED);
    }

    if (customerOrder.get().getAmount() < entry.getAmount()) {
      throw new CustomException(String.format("It is not possible to make the payment because the amount sent ($%s) is more than the amount of the service ($%s).",
              entry.getAmount(), customerOrder.get().getAmount()), HttpStatus.PRECONDITION_FAILED);
    }

    if (entry.getType() == PaymentType.CASH && entry.getInstallments() > 1) {
      throw new CustomException("It is not possible to make more than one installment for the type of payment requested.", HttpStatus.PRECONDITION_FAILED);
    }

    if (entry.getInstallments() < 1) {
      throw new CustomException("Invalid number of installments.", HttpStatus.PRECONDITION_FAILED);
    }

    Payment payment = new Payment()
            .setDate(LocalDate.now())
            .setAmount(entry.getAmount())
            .setType(entry.getType())
            .setInstallments(entry.getInstallments())
            .setStatus(PaymentStatus.PROCESSING)
            .setCustomerOrder(customerOrder.get());

    payment = paymentRepository.save(payment);

    return PaymentMapper.toPaymentDto(payment);
  }

  /**
   * Finds a payment by his id
   */
  @Override
  public PaymentReadDto findById(Long id) {
    Optional<Payment> payment = paymentRepository.findById(id);

    return payment.map(PaymentMapper::toPaymentDto).orElse(null);
  }

  /**
   * Finds a payment by his id, but only the ones that refer to the specific user that currently is searching
   */
  @Override
  public PaymentReadDto findUserPaymentById(Long userId, Long id) {
    Optional<Payment> payment = paymentRepository.findPaymentById(userId, id);

    return payment.map(PaymentMapper::toPaymentDto).orElse(null);
  }

  /**
   * Returns all payments
   */
  @Override
  public List<PaymentReadDto> findAll() {
    List<Payment> allPayments = paymentRepository.findAll();

    return allPayments.stream()
            .map(PaymentMapper::toPaymentDto)
            .collect(Collectors.toList());
  }

  /**
   * Returns all payments of a specific user, but only the ones that refer to the specific user that currently is searching
   */
  @Override
  public List<PaymentReadDto> findAllByUser(Long userId) {
    List<Payment> allPayments = paymentRepository.findAllByUser(userId);

    return allPayments.stream()
            .map(PaymentMapper::toPaymentDto)
            .collect(Collectors.toList());
  }

  /**
   * Returns payments that still have 'processing' status
   */
  @Override
  public List<PaymentReadDto> getPendingPayments(int limit) {
    List<Payment> allPendingPayments = paymentRepository.getPendingPayments(limit);

    return allPendingPayments.stream()
            .map(PaymentMapper::toPaymentDto)
            .collect(Collectors.toList());
  }
}