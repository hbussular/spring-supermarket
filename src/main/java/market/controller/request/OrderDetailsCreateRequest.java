package market.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel
public class OrderDetailsCreateRequest {

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @NotNull(message = "This field is required")
    private int quantity;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @NotNull(message = "This field is required")
    private long productId;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @NotNull(message = "This field is required")
    private long customerOrderId;
}