package market.service;

import market.dto.model.customerOrder.CustomerOrderReadDto;
import market.dto.model.payment.PaymentCreateDto;
import market.dto.model.payment.PaymentDto;
import market.dto.model.payment.PaymentReadDto;

import java.util.List;


public interface PaymentService {
    PaymentReadDto create(PaymentDto entry, Long customerOrderId);
    PaymentReadDto findById(Long id);
    List<PaymentReadDto> findAll();
    PaymentReadDto findUserPaymentById(Long userId, Long id);
    List<PaymentReadDto> findAllByUser(Long userId);
    List<PaymentReadDto> getPendingPayments(int limit);
}