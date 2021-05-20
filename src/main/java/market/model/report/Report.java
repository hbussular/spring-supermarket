package market.model.report;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "report",
        indexes = @Index(
                name = "idx_report_id",
                columnList = "report_id",
                unique = true
        ))
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Report {
    @Id
    @Column(name = "report_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, name = "report_name")
    private String name;

    @Column(name = "report_date")
    private LocalDate date;

    @Column(name = "report_data")
    private String data;

    @Column(name = "report_type", nullable = false)
    private ReportType type;

    @Column(name = "report_status", nullable = false)
    private ReportStatus status;
}