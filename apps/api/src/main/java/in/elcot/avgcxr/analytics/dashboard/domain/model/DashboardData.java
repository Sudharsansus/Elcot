package in.elcot.avgcxr.analytics.dashboard.domain.model;

import in.elcot.avgcxr.platformcore.model.AuditableEntity;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Domain aggregate root for Real-time KPI aggregations: user counts, application status breakdowns,
 * district distributions, trends. This class contains business logic and validation rules. It has
 * NO dependencies on Spring or infrastructure.
 */
/**
 * Pure domain model — no framework imports. Persistence is handled by
 * infrastructure.persistence.entity + mapper.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DashboardData extends AuditableEntity {

  private UUID id;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  public static DashboardData create() {
    return DashboardData.builder()
        .id(UUID.randomUUID())
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }
}
