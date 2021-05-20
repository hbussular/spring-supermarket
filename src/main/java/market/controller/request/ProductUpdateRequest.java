package market.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel
public class ProductUpdateRequest extends ProductCreateRequest {
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    @NotNull(message = "This field is required")
    private Long id;
}