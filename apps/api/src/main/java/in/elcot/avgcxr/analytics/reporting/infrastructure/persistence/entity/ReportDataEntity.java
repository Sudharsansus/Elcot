package in.elcot.avgcxr.analytics.reporting.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "district_statistics")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReportDataEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "district", length = 100)
    private String district;

    @Column(name = "period", length = 50)
    private String period;

    @Column(name = "metric", length = 100)
    private String metric;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
