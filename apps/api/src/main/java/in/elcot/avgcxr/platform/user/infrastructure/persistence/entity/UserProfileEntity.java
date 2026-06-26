package in.elcot.avgcxr.platform.user.infrastructure.persistence.entity;

import in.elcot.avgcxr.common.infrastructure.security.EncryptedLocalDateConverter;
import in.elcot.avgcxr.common.infrastructure.security.EncryptedStringConverter;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileEntity {
  @Id
  @Column(name = "id")
  private UUID id;

  @Column(name = "user_id")
  private UUID userId;

  @Convert(converter = EncryptedLocalDateConverter.class)
  @Column(name = "date_of_birth", columnDefinition = "text")
  private LocalDate dateOfBirth;

  @Column(name = "gender", length = 10)
  private String gender;

  @Convert(converter = EncryptedStringConverter.class)
  @Column(name = "address", columnDefinition = "TEXT")
  private String address;

  @Column(name = "district", length = 100)
  private String district;

  @Column(name = "state", length = 100)
  private String state;

  @Convert(converter = EncryptedStringConverter.class)
  @Column(name = "pincode", columnDefinition = "TEXT")
  private String pincode;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
}
