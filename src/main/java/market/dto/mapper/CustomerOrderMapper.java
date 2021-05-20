package market.dto.mapper;

import market.dto.model.customerOrder.CustomerOrderReadDto;
import market.model.customerOrder.CustomerOrder;
import org.springframework.stereotype.Component;


@Component
public class CustomerOrderMapper {
    public static CustomerOrderReadDto toCustomerOrderDto(CustomerOrder customerOrder) {
        return new CustomerOrderReadDto()
            .setId(customerOrder.getId())
            .setStatus(customerOrder.getStatus())
            .setDate(customerOrder.getDate())
            .setAmount(customerOrder.getAmount());
    }

}