package market.dto.mapper;


import market.dto.model.orderDetails.OrderDetailsReadDto;
import market.model.orderDetails.OrderDetails;
import org.springframework.stereotype.Component;


@Component
public class OrderDetailsMapper {
    public static OrderDetailsReadDto toOrderDetailsDto(OrderDetails orderDetails) {
        return new OrderDetailsReadDto()
            .setId(orderDetails.getId())
            .setProductId(orderDetails.getProduct().getId())
            .setQuantity(orderDetails.getQuantity());
    }

}
