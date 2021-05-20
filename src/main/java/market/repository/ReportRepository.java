package market.repository;

import market.model.report.Report;
import market.model.report.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findById(Long id);

    @Transactional
    void deleteById(Long id);

    @Query(value = "SELECT * FROM report WHERE report_status = 1 LIMIT ?1", nativeQuery = true)
    List<Report> getPendingReports(int limit);

    @Query(value = "SELECT * FROM report WHERE report_type = ? AND report_status = 1 LIMIT 1", nativeQuery = true)
    Optional<Report> findLatestProcessedByType(ReportType type);
}