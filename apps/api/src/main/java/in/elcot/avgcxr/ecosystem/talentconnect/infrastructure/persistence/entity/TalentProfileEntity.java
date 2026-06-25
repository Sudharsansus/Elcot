package in.elcot.avgcxr.ecosystem.talentconnect.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "talent_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TalentProfileEntity {
  @Id
  @Column(name = "id")
  private UUID id;

  @Column(name = "user_id")
  private UUID userId;

  @Column(name = "name", length = 200)
  private String name;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "category", length = 100)
  private String category;

  @Column(name = "experience_years")
  private Integer experienceYears;

  @Column(name = "portfolio_url", length = 500)
  private String portfolioUrl;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
}
