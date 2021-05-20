package market.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import market.model.report.ReportType;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel
public class ReportCreateRequest {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull(message = "This field is required")
    @ApiModelProperty(notes = "Types available: TOP15PRODUCTS, TOTAL_TRANSACTION_MONTH")
    private ReportType type;
}
