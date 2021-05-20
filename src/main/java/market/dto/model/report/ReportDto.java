package market.dto.model.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import market.model.report.ReportStatus;
import market.model.report.ReportType;
import springfox.documentation.spring.web.json.Json;

import java.time.LocalDate;


@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportDto {
    private Long id;
    private String name;
    private LocalDate date;
    private String data;
    private ReportType type;
    private ReportStatus status;
}
