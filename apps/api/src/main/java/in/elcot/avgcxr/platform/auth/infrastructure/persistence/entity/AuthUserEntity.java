package in.elcot.avgcxr.platform.auth.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserEntity {
  @Id
  @Column(name = "id")
  private UUID id;

  @Column(name = "email", nullable = false, unique = true, length = 255)
  private String email;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Column(name = "phone", length = 20)
  private String phone;

  @Column(name = "full_name", nullable = false, length = 200)
  private String fullName;

  @Column(name = "tamil_name", length = 200)
  private String tamilName;

  @Column(name = "is_verified", nullable = false)
  private boolean verified;

  @Column(name = "is_active", nullable = false)
  private boolean active;

  @Column(name = "last_login_at")
  private LocalDateTime lastLoginAt;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
}
