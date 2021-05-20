package market.dto.mapper;


import market.dto.model.alert.AlertReadDto;
import market.dto.model.report.ReportReadDto;
import market.model.alert.Alert;
import market.model.report.Report;
import org.springframework.stereotype.Component;


@Component
public class AlertMapper {
    public static AlertReadDto toAlertDto(Alert alert) {
        return new AlertReadDto()
                .setId(alert.getId())
                .setDate(alert.getDate())
                .setType(alert.getType())
                .setMessage(alert.getMessage());
    }
}