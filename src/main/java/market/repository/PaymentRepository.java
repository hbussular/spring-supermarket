package market.repository;


import market.model.payment.Payment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findById(Long id);

    @Transactional
    void deleteById(Long id);

    @Query(value = "SELECT * FROM payment pa INNER JOIN customer_order co ON pa.customer_order_id = co.customer_order_id WHERE co.user_id = ?1 AND pa.payment_id = ?2", nativeQuery = true)
    Optional<Payment> findPaymentById(Long userId, Long id);

    @Query(value = "SELECT * FROM payment pa INNER JOIN customer_order co ON pa.customer_order_id = co.customer_order_id WHERE co.user_id = ?1", nativeQuery = true)
    List<Payment> findAllByUser(Long userId);

    @Query(value = "SELECT pa.*, co.* FROM payment pa INNER JOIN customer_order co ON pa.customer_order_id = co.customer_order_id WHERE pa.payment_status = 3 LIMIT ?1", nativeQuery = true)
    List<Payment> getPendingPayments(int limit);
}