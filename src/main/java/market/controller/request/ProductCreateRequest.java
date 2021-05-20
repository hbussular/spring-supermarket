package market.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import market.model.product.ProductCategory;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel
public class ProductCreateRequest {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull(message = "This field is required")
    @ApiModelProperty(notes = "Types available: CASH, CREDIT CARD")
    private ProductCategory category;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    @NotNull(message = "This field is required")
    private int quantity;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
    @NotNull(message = "This field is required")
    private int price;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull(message = "This field is required")
    private String name;
}