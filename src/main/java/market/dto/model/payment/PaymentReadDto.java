package market.dto.model.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import market.model.payment.PaymentStatus;
import market.model.payment.PaymentType;

import java.time.LocalDate;


@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentReadDto {
    private Long id;
    private LocalDate date;
    private PaymentType type;
    private PaymentStatus status;
    private double amount;
    private int installments;
    private Long customerOrderId;
}