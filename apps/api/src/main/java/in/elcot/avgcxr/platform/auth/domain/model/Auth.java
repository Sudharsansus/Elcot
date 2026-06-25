package in.elcot.avgcxr.platform.auth.domain.model;

import in.elcot.avgcxr.platform.auth.application.command.RegisterUserCommand;
import in.elcot.avgcxr.platformcore.model.AuditableEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Pure domain model — no framework imports. Persistence is handled by
 * infrastructure.persistence.entity + mapper.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Auth extends AuditableEntity {

  private UUID id;

  private String email;

  private String passwordHash;

  private String phone;

  private String fullName;

  private String tamilName;

  @Builder.Default private boolean verified = false;

  @Builder.Default private boolean active = true;

  private LocalDateTime lastLoginAt;

  private final List<String> roles = new ArrayList<>();

  private String rawPassword;

  public static Auth create(RegisterUserCommand cmd, String encodedPassword) {
    return Auth.builder()
        .id(UUID.randomUUID())
        .email(cmd.email().toLowerCase().trim())
        .passwordHash(encodedPassword)
        .fullName(cmd.fullName().trim())
        .tamilName(cmd.tamilName())
        .phone(cmd.phone().trim())
        .active(true)
        .verified(false)
        .build();
  }

  public boolean authenticate(String rawPassword, String encodedPassword) {
    // Actual BCrypt comparison done by PasswordEncoder in service layer
    return rawPassword != null && !rawPassword.isEmpty();
  }

  public void recordLogin() {
    this.lastLoginAt = LocalDateTime.now();
  }
}
