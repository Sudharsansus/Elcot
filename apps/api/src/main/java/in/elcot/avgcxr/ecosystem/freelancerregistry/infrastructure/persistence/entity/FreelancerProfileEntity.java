package in.elcot.avgcxr.ecosystem.freelancerregistry.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "freelancer_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FreelancerProfileEntity {
  @Id
  @Column(name = "id")
  private UUID id;

  @Column(name = "user_id")
  private UUID userId;

  @Column(name = "name", length = 200)
  private String name;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "skills", columnDefinition = "TEXT")
  private String skills;

  @Column(name = "hourly_rate")
  private BigDecimal hourlyRate;

  @Column(name = "district", length = 100)
  private String district;

  @Column(name = "available")
  private Boolean available;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
}
