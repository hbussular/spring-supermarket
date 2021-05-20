package market.model.alert;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import market.model.customerOrder.CustomerOrder;
import market.model.payment.PaymentStatus;
import market.model.payment.PaymentType;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "alert")
public class Alert {
    @Id
    @Column(name = "alert_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alert_date")
    private LocalDate date;

    @Column(name = "alert_type")
    private AlertType type;

    @Column(name = "alert_message")
    private String message;
}
