package market.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import market.dto.mapper.PaymentMapper;
import market.dto.mapper.ReportMapper;
import market.dto.model.payment.PaymentReadDto;
import market.dto.model.report.ReportDto;
import market.dto.model.report.ReportReadDto;
import market.exception.CustomException;
import market.model.payment.Payment;
import market.model.report.Report;
import market.model.report.ReportStatus;
import market.dto.model.report.ReportTopProductDto;
import market.model.report.ReportType;
import market.repository.CustomerOrderRepository;
import market.repository.ReportRepository;
import market.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ReportServiceImpl implements ReportService {

  @Autowired
  private ReportRepository reportRepository;

  @Autowired
  private CustomerOrderRepository customerOrderRepository;

  @Autowired
  private JdbcTemplate jtm;

  /**
   * Creates a report
   */
  @Override
  public ReportReadDto create(ReportDto entry) {
    Report report = new Report()
            .setDate(LocalDate.now())
            .setType(entry.getType())
            .setName(entry.getType().name())
            .setStatus(ReportStatus.PENDING);

    report = reportRepository.save(report);

    return ReportMapper.toReportDto(report);
  }

  /**
   * Finds a report by his id
   */
  @Override
  public ReportReadDto findById(Long id) {
    Optional<Report> report = reportRepository.findById(id);

    return report.map(ReportMapper::toReportDto).orElse(null);
  }

  /**
   * Returns all reports
   */
  @Override
  public List<ReportReadDto> findAll() {
    List<Report> allReports = reportRepository.findAll();

    return allReports.stream()
            .map(ReportMapper::toReportDto)
            .collect(Collectors.toList());
  }

  /**
   * Returns latest reports by type
   */
  @Override
  public ReportReadDto findLatestByType(ReportType type) {
    Optional<Report> report = reportRepository.findLatestProcessedByType(type);

    if (!report.isPresent()) {
      throw new CustomException("There is no processed report for the requested type.", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    return report.map(ReportMapper::toReportDto).orElse(null);
  }

  /**
   * Processes pending reports
   */
  @Override
  public void processPendingReports() throws JsonProcessingException {
    int LIMIT_OF_REPORTS_PER_TIME = 10;
    List<Report> allPendingReports = reportRepository.getPendingReports(LIMIT_OF_REPORTS_PER_TIME);
    ObjectMapper objectMapper = new ObjectMapper();

    for (Report report : allPendingReports) {
      if (report.getType() == ReportType.TOP15PRODUCTS) {
        List<ReportTopProductDto> reportTopProductList = jtm.query(
                "SELECT p.product_id as productId, p.product_name as productName, sum(od.order_details_quantity) as total FROM product p " +
                        "INNER JOIN order_details as od on p.product_id = od.product_id GROUP BY p.product_id LIMIT ?",
                new Object[] { 15 },
                (rs, rowNum) ->
                        new ReportTopProductDto()
                                .setProductId(rs.getLong("productId"))
                                .setProductName(rs.getString("productName"))
                                .setTotal(rs.getInt("total"))
        );

        report.setData(objectMapper.writeValueAsString(reportTopProductList));
        report.setStatus(ReportStatus.PROCESSED);
        report.setDate(LocalDate.now());
        reportRepository.save(report);
      }
    }
  }

  /**
   * Returns pending reports
   */
  @Override
  public List<ReportReadDto> getPendingReports(int limit) {
    List<Report> allPendingReports = reportRepository.getPendingReports(limit);

    return allPendingReports.stream()
            .map(ReportMapper::toReportDto)
            .collect(Collectors.toList());
  }
}