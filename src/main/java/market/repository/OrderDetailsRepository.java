package market.repository;

import market.model.orderDetails.OrderDetails;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    @EntityGraph(attributePaths = {"product", "product.id", "product.price"})
    List<OrderDetails> findAllByCustomerOrderId(Long id);

    @Transactional
    void deleteById(Long id);

    @Query(value = "SELECT * FROM order_details od INNER JOIN customer_order co ON od.customer_order_id = co.customer_order_id WHERE co.user_id = ?1 AND od.order_details_id = ?2", nativeQuery = true)
    Optional<OrderDetails> findOrderDetailsById(Long userId, Long id);

    @Query(value = "SELECT * FROM order_details od INNER JOIN customer_order co ON od.customer_order_id = co.customer_order_id WHERE co.user_id = ?1", nativeQuery = true)
    List<OrderDetails> findAllByUser(Long userId);
}