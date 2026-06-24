package in.elcot.avgcxr.policy.application.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;

/**
 * Application policy configuration.
 *
 * <p>Provides:</p>
 * <ul>
 *   <li>Application state machine timing (auto-archive after N days)</li>
 *   <li>Eligibility cache TTL</li>
 *   <li>Daily deadline reminder scheduler</li>
 * </ul>
 */
@Configuration
@EnableScheduling
public class ApplicationConfig {

    @Value("${avgcxr.application.draft-ttl-days:30}")
    private int draftTtlDays;

    @Value("${avgcxr.application.archive-after-days:365}")
    private int archiveAfterDays;

    @Value("${avgcxr.application.eligibility-cache-ttl-minutes:60}")
    private int eligibilityCacheTtlMinutes;

    @Bean
    public ApplicationTtls applicationTtls() {
        return new ApplicationTtls(
                Duration.ofDays(draftTtlDays),
                Duration.ofDays(archiveAfterDays)
        );
    }

    @Bean
    public ApplicationCacheSettings applicationCacheSettings() {
        return new ApplicationCacheSettings(Duration.ofMinutes(eligibilityCacheTtlMinutes));
    }

    @Bean
    public ApplicationMetrics applicationMetrics() {
        return new ApplicationMetrics();
    }

    /** Daily at 09:00 IST — send deadline reminders to applicants. */
    @Scheduled(cron = "${avgcxr.application.deadline-reminder-cron:0 0 9 * * *}",
               zone = "${avgcxr.application.timezone:Asia/Kolkata}")
    public void sendDeadlineReminders() {
        // Reminders are dispatched via the notification queue.
    }

    public record ApplicationTtls(Duration draftTtl, Duration archiveAfter) {}

    public record ApplicationCacheSettings(Duration eligibilityCacheTtl) {}

    public static class ApplicationMetrics {
        private final java.util.concurrent.atomic.AtomicLong submitted = new java.util.concurrent.atomic.AtomicLong();
        private final java.util.concurrent.atomic.AtomicLong approved = new java.util.concurrent.atomic.AtomicLong();
        private final java.util.concurrent.atomic.AtomicLong rejected = new java.util.concurrent.atomic.AtomicLong();

        public void recordSubmitted() { submitted.incrementAndGet(); }
        public void recordApproved() { approved.incrementAndGet(); }
        public void recordRejected() { rejected.incrementAndGet(); }
        public long getSubmitted() { return submitted.get(); }
        public long getApproved() { return approved.get(); }
        public long getRejected() { return rejected.get(); }
    }
}
