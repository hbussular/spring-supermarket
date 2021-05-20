package market.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import market.model.customerOrder.CustomerOrderStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel
public class CustomerOrderCreateRequest {
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @NotNull(message = "This field is required")
    private double amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull(message = "This field is required")
    private LocalDate date;
}