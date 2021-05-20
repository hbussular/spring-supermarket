package market.serviceImpl;

import market.dto.mapper.PaymentMapper;
import market.dto.mapper.ProductMapper;
import market.dto.model.payment.PaymentReadDto;
import market.dto.model.product.ProductCreateDto;
import market.dto.model.product.ProductDto;
import market.dto.model.product.ProductReadDto;
import market.exception.CustomException;
import market.model.payment.Payment;
import market.model.product.Product;
import market.repository.CustomerOrderRepository;
import market.repository.ProductRepository;
import market.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CustomerOrderRepository customerOrderRepository;


  /**
   * Creates a product
   */
  @Override
  public ProductReadDto create(ProductCreateDto entry) {
    Product product = new Product()
            .setName(entry.getName())
            .setCategory(entry.getCategory())
            .setPrice(entry.getPrice())
            .setQuantity(entry.getQuantity());

    product = productRepository.save(product);
    return ProductMapper.toProductDto(product);
  }

  /**
   * Finds a product by his id
   */
  @Override
  public ProductReadDto findById(Long id) {
    Optional<Product> product = productRepository.findById(id);

    return product.map(ProductMapper::toProductDto).orElse(null);
  }

  /**
   * Returns all products
   */
  @Override
  public List<ProductReadDto> findAll() {
    List<Product> allProducts = productRepository.findAll();

    return allProducts.stream()
            .map(ProductMapper::toProductDto)
            .collect(Collectors.toList());
  }

  @Override
  public boolean hasAvailableStock(Long id, int quantityToSell) {
    Optional<Product> product = productRepository.findById(id);
    return product.filter(value -> quantityToSell < value.getQuantity()).isPresent();
  }

  /**
   * Updates a product
   */
  @Override
  public ProductReadDto update(ProductDto entry) {
    Optional<Product> product = productRepository.findById(entry.getId());

    if (!product.isPresent()) {
      throw new CustomException("The product does not exist.", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    Product productUpdated = product.get();

    if (entry.getName() != null && !entry.getName().isEmpty()) {
      productUpdated.setName(entry.getName());
    }

    if (entry.getPrice() != 0 && entry.getPrice() > 0) {
      productUpdated.setPrice(entry.getPrice());
    }

    if (entry.getQuantity() >= 0) {
      productUpdated.setQuantity(entry.getQuantity());
    }

    if (entry.getCategory() != null && !entry.getCategory().toString().isEmpty()) {
      productUpdated.setCategory(entry.getCategory());
    }

    productUpdated = productRepository.save(productUpdated);

    return ProductMapper.toProductDto(productUpdated);
  }

}