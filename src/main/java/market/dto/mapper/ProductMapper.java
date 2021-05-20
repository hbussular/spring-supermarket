package market.dto.mapper;


import market.dto.model.product.ProductReadDto;
import market.model.product.Product;
import org.springframework.stereotype.Component;


@Component
public class ProductMapper {
    public static ProductReadDto toProductDto(Product product) {
        return new ProductReadDto()
            .setId(product.getId())
            .setName(product.getName())
            .setPrice(product.getPrice())
            .setQuantity(product.getQuantity())
            .setCategory(product.getCategory());
    }
}
