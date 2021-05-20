package market.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import market.dto.model.report.ReportDto;
import market.dto.model.report.ReportReadDto;
import market.model.report.ReportType;

import java.util.List;


public interface ReportService {
    ReportReadDto create(ReportDto entry);
    ReportReadDto findById(Long id);
    List<ReportReadDto> findAll();
    ReportReadDto findLatestByType(ReportType type);
    List<ReportReadDto> getPendingReports(int limit);
    void processPendingReports() throws JsonProcessingException;
}