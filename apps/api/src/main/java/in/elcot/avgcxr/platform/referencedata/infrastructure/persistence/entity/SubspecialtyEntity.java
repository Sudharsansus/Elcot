package in.elcot.avgcxr.platform.referencedata.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "subspecialties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubspecialtyEntity {
  @Id
  @Column(name = "code", length = 10)
  private String code;

  @Column(name = "name_en", length = 100, nullable = false)
  private String nameEn;

  @Column(name = "name_ta", length = 200, nullable = false)
  private String nameTa;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
}
