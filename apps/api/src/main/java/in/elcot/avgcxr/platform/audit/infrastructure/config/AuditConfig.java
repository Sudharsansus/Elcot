package in.elcot.avgcxr.platform.audit.infrastructure.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Audit configuration.
 *
 * <p>Provides:
 *
 * <ul>
 *   <li>Audit log retention (default 7 years — DPDP/IT-Act compliance)
 *   <li>Asynchronous audit writer (non-blocking)
 *   <li>Daily partition rollover scheduler
 * </ul>
 */
@Configuration
@EnableScheduling
public class AuditConfig {

  @Value("${avgcxr.audit.retention-years:7}")
  private int retentionYears;

  @Value("${avgcxr.audit.async:true}")
  private boolean asyncEnabled;

  @Bean
  public AuditRetentionPolicy auditRetentionPolicy() {
    return new AuditRetentionPolicy(Duration.ofDays(retentionYears * 365L));
  }

  @Bean
  public AuditSettings auditSettings() {
    return new AuditSettings(asyncEnabled);
  }

  /** Daily at 01:00 — rotate audit log partitions. */
  @Scheduled(cron = "${avgcxr.audit.partition-rollover-cron:0 0 1 * * *}")
  public void rolloverPartitions() {
    // Partition rollover is handled by PostgreSQL pg_partman / pg_cron in production.
  }

  public record AuditRetentionPolicy(Duration retention) {}

  public record AuditSettings(boolean asyncEnabled) {}
}
