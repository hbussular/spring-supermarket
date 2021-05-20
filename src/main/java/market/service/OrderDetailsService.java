package market.service;

import market.dto.model.orderDetails.OrderDetailsDto;
import market.dto.model.orderDetails.OrderDetailsReadDto;

import java.util.List;
import java.util.Optional;


public interface OrderDetailsService {
    OrderDetailsReadDto create(OrderDetailsDto entry);
    OrderDetailsReadDto findById(Long id);
    List<OrderDetailsReadDto> findAll();
    OrderDetailsReadDto findUserOrderDetailsById(Long userId, Long id);
    List<OrderDetailsReadDto> findAllByUser(Long userId);
    OrderDetailsReadDto update(OrderDetailsDto entry, Long userId, boolean byPass);
}