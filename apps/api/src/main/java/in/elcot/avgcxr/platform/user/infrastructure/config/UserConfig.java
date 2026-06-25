package in.elcot.avgcxr.platform.user.infrastructure.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * User platform configuration.
 *
 * <p>Provides:
 *
 * <ul>
 *   <li>Password policy (min length, complexity, rotation)
 *   <li>Account lockout policy (5 attempts, 15 min lockout — DPDP best practice)
 *   <li>Email verification TTL
 * </ul>
 */
@Configuration
public class UserConfig {

  @Value("${avgcxr.user.password.min-length:8}")
  private int passwordMinLength;

  @Value("${avgcxr.user.password.rotation-days:90}")
  private int passwordRotationDays;

  @Value("${avgcxr.user.lockout.max-attempts:5}")
  private int lockoutMaxAttempts;

  @Value("${avgcxr.user.lockout.duration-minutes:15}")
  private int lockoutDurationMinutes;

  @Value("${avgcxr.user.email-verification-ttl-hours:24}")
  private int emailVerificationTtlHours;

  @Bean
  public PasswordPolicy passwordPolicy() {
    return new PasswordPolicy(passwordMinLength, Duration.ofDays(passwordRotationDays));
  }

  @Bean
  public LockoutPolicy lockoutPolicy() {
    return new LockoutPolicy(lockoutMaxAttempts, Duration.ofMinutes(lockoutDurationMinutes));
  }

  @Bean
  public EmailVerificationTtl emailVerificationTtl() {
    return new EmailVerificationTtl(Duration.ofHours(emailVerificationTtlHours));
  }

  public record PasswordPolicy(int minLength, Duration rotation) {}

  public record LockoutPolicy(int maxAttempts, Duration lockoutDuration) {}

  public record EmailVerificationTtl(Duration ttl) {}
}
