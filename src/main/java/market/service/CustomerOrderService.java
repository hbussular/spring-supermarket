package market.service;

import market.dto.model.customerOrder.CustomerOrderDto;
import market.dto.model.customerOrder.CustomerOrderReadDto;

import java.util.List;


public interface CustomerOrderService {
    CustomerOrderReadDto create(CustomerOrderDto entry);
    CustomerOrderReadDto findById(Long id);
    List<CustomerOrderReadDto> findAll();
    CustomerOrderReadDto findUserOrderById(Long userId, Long id);
    List<CustomerOrderReadDto> findAllByUser(Long userId);
    void recalculateAmountById(Long id);
    CustomerOrderReadDto checkout(Long id, Long paymentId);
}