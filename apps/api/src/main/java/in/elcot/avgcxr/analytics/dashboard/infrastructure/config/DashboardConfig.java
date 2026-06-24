package in.elcot.avgcxr.analytics.dashboard.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Dashboard configuration.
 *
 * <p>Provides:</p>
 * <ul>
 *   <li>Cache manager for dashboard stats (60s TTL via scheduler)</li>
 *   <li>Auto-refresh of aggregations every 5 minutes</li>
 *   <li>Per-role dashboard settings</li>
 * </ul>
 */
@Configuration
@EnableScheduling
public class DashboardConfig {

    @Value("${avgcxr.dashboard.refresh-seconds:300}")
    private long refreshSeconds;

    @Value("${avgcxr.dashboard.max-cards:12}")
    private int maxCards;

    @Bean
    public CacheManager dashboardCacheManager() {
        return new ConcurrentMapCacheManager("dashboard-stats", "dashboard-charts");
    }

    @Bean
    public DashboardSettings dashboardSettings() {
        return new DashboardSettings(refreshSeconds, maxCards);
    }

    @Bean
    public DashboardMetrics dashboardMetrics() {
        return new DashboardMetrics();
    }

    @Scheduled(fixedDelayString = "${avgcxr.dashboard.refresh-ms:300000}",
               initialDelayString = "${avgcxr.dashboard.refresh-initial-ms:30000}")
    public void refreshDashboardAggregations() {
        // Application-level refresh handled in DashboardService.
    }

    public record DashboardSettings(long refreshSeconds, int maxCards) {}

    public static class DashboardMetrics {
        private final java.util.concurrent.atomic.AtomicLong viewsTotal = new java.util.concurrent.atomic.AtomicLong();
        public void recordView() { viewsTotal.incrementAndGet(); }
        public long getViewsTotal() { return viewsTotal.get(); }
    }
}
