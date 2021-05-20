package market.model.customerOrder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import market.model.orderDetails.OrderDetails;
import market.model.product.Product;
import market.model.user.Role;
import market.model.user.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "customer_order")
public class CustomerOrder {
    @Id
    @Column(name = "customer_order_id")
    @Access(AccessType.PROPERTY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_order_date")
    private LocalDate date;

    @Column(name = "customer_order_status")
    private CustomerOrderStatus status;

    @Column(name = "customer_order_amount")
    private double amount;

    @ElementCollection(fetch = FetchType.EAGER)
    List<OrderDetails> orderDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
