package in.elcot.avgcxr.analytics.dashboard.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "daily_statistics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDataEntity {
  @Id
  @Column(name = "id")
  private UUID id;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
}
