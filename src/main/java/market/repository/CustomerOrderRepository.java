package market.repository;

import market.model.customerOrder.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    Optional<CustomerOrder> findById(Long id);

    @Transactional
    void deleteById(Long id);

    @Query(value = "SELECT * FROM customer_order WHERE user_id = ?1 AND customer_order_id = ?2", nativeQuery = true)
    Optional<CustomerOrder> findCustomerOrderById(Long userId, Long id);

    @Query(value = "SELECT * FROM customer_order WHERE user_id = ?1", nativeQuery = true)
    List<CustomerOrder> findAllByUser(Long userId);
}