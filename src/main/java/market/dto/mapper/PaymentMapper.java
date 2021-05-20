package market.dto.mapper;

import market.dto.model.payment.PaymentReadDto;
import market.model.payment.Payment;
import org.springframework.stereotype.Component;


@Component
public class PaymentMapper {
    public static PaymentReadDto toPaymentDto(Payment payment) {
        return new PaymentReadDto()
            .setId(payment.getId())
            .setDate(payment.getDate())
            .setAmount(payment.getAmount())
            .setType(payment.getType())
            .setStatus(payment.getStatus())
            .setInstallments(payment.getInstallments())
            .setCustomerOrderId(payment.getCustomerOrder().getId());
    }

}