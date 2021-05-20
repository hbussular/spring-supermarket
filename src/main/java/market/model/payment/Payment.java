package market.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import market.model.customerOrder.CustomerOrder;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_date")
    private LocalDate date;

    @Column(name = "payment_type")
    private PaymentType type;

    @Column(name = "payment_status")
    private PaymentStatus status;

    @Column(name = "payment_amount")
    private double amount;

    @Column(name = "payment_installments")
    private int installments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_order_id")
    private CustomerOrder customerOrder;
}
