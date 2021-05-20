package market.repository;

import javax.transaction.Transactional;

import market.model.payment.Payment;
import market.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findById(Long id);

    @Transactional
    void deleteById(Long id);

    @Query(value = "SELECT p.product_id as productId, p.product_name as productName FROM product p WHERE p.quantity_available <= ?1", nativeQuery = true)
    List<Product> findAllLowStockProducts(int lessThanNumber);
}