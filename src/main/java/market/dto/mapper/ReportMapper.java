package market.dto.mapper;


import market.dto.model.report.ReportReadDto;
import market.model.report.Report;
import org.springframework.stereotype.Component;


@Component
public class ReportMapper {
    public static ReportReadDto toReportDto(Report report) {
        return new ReportReadDto()
                .setId(report.getId())
                .setName(report.getName())
                .setDate(report.getDate())
                .setType(report.getType())
                .setData(report.getData())
                .setStatus(report.getStatus());
    }
}