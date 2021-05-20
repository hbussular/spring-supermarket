package market.service;

import market.dto.model.payment.PaymentReadDto;
import market.dto.model.product.ProductCreateDto;
import market.dto.model.product.ProductDto;
import market.dto.model.product.ProductReadDto;

import java.util.List;


public interface ProductService {
    ProductReadDto create(ProductCreateDto entry);
    ProductReadDto findById(Long id);
    ProductReadDto update(ProductDto entry);
    List<ProductReadDto> findAll();
    boolean hasAvailableStock(Long id, int quantityToSell);

}