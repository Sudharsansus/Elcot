package in.elcot.avgcxr.policy.scheme.infrastructure.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Scheme policy configuration.
 *
 * <p>Provides:
 *
 * <ul>
 *   <li>Scheme lifecycle TTLs (DRAFT, PUBLISHED, RETIRED)
 *   <li>Auto-close expired schemes scheduler (daily 02:30 IST)
 *   <li>Subsidy computation defaults
 * </ul>
 */
@Configuration
@EnableScheduling
public class SchemeConfig {

  @Value("${avgcxr.scheme.draft-ttl-days:14}")
  private int draftTtlDays;

  @Value("${avgcxr.scheme.archive-after-retirement-days:730}")
  private int archiveAfterRetirementDays;

  @Value("${avgcxr.scheme.max-subsidy-inr:5000000}")
  private long maxSubsidyInr;

  @Bean
  public SchemeLifecycles schemeLifecycles() {
    return new SchemeLifecycles(
        Duration.ofDays(draftTtlDays), Duration.ofDays(archiveAfterRetirementDays));
  }

  @Bean
  public SchemeLimits schemeLimits() {
    return new SchemeLimits(maxSubsidyInr);
  }

  /** Daily at 02:30 IST — auto-close expired published schemes. */
  @Scheduled(
      cron = "${avgcxr.scheme.auto-close-cron:0 30 2 * * *}",
      zone = "${avgcxr.scheme.timezone:Asia/Kolkata}")
  public void autoCloseExpiredSchemes() {
    // Implemented in SchemeUseCaseService.autoCloseExpired().
  }

  public record SchemeLifecycles(Duration draftTtl, Duration archiveAfterRetirement) {}

  public record SchemeLimits(long maxSubsidyInr) {}
}
