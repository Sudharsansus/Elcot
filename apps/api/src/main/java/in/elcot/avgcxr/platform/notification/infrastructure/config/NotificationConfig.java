package in.elcot.avgcxr.platform.notification.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Notification service configuration.
 *
 * <p>Provides:</p>
 * <ul>
 *   <li>Channel priorities (EMAIL, SMS, PUSH, IN_APP)</li>
 *   <li>Retry policy (default 3 attempts with exponential backoff)</li>
 *   <li>Templating defaults</li>
 * </ul>
 */
@Configuration
public class NotificationConfig {

    @Value("${avgcxr.notification.retry-attempts:3}")
    private int retryAttempts;

    @Value("${avgcxr.notification.retry-backoff-base-ms:1000}")
    private long retryBackoffBaseMs;

    @Value("${avgcxr.notification.channels:EMAIL,SMS,PUSH,IN_APP}")
    private String channels;

    @Bean
    public NotificationChannels notificationChannels() {
        return new NotificationChannels(java.util.Arrays.asList(channels.split(",")).stream()
                .map(String::trim).filter(s -> !s.isEmpty()).toList());
    }

    @Bean
    public RetryPolicy notificationRetryPolicy() {
        return new RetryPolicy(retryAttempts, Duration.ofMillis(retryBackoffBaseMs));
    }

    public record NotificationChannels(java.util.List<String> channels) {}

    public record RetryPolicy(int maxAttempts, Duration backoffBase) {
        public Duration backoffFor(int attempt) {
            return backoffBase.multipliedBy(1L << Math.min(attempt, 6));
        }
    }
}
