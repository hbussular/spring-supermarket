package market.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import market.model.payment.PaymentStatus;
import market.model.payment.PaymentType;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel
public class PaymentCreateRequest {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull(message = "This field is required")
    @ApiModelProperty(notes = "Types available: CASH, CREDIT CARD")
    private PaymentType type;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
    @NotNull(message = "This field is required")
    private double amount;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    @NotNull(message = "This field is required")
    @ApiModelProperty(notes = "Minimum 1 and Maximum 10 installments")
    private int installments;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    @NotNull(message = "This field is required")
    private Long customerOrderId;
}