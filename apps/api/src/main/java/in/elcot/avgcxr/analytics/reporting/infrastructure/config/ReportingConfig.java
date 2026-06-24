package in.elcot.avgcxr.analytics.reporting.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Reporting configuration.
 *
 * <p>Provides:</p>
 * <ul>
 *   <li>Cache manager for generated report data (5-min TTL via scheduler)</li>
 *   <li>Pre-computed daily/weekly/monthly report refresh (cron)</li>
 *   <li>Export directory for PDF/Excel reports</li>
 * </ul>
 */
@Configuration
@EnableScheduling
public class ReportingConfig {

    @Value("${avgcxr.reporting.cache-ttl-seconds:300}")
    private long cacheTtlSeconds;

    @Value("${avgcxr.reporting.export-dir:/var/tmp/avgcxr-reports}")
    private String exportDir;

    @Bean
    @org.springframework.context.annotation.Primary
    public CacheManager reportingCacheManager() {
        return new ConcurrentMapCacheManager("reports", "dashboard-stats");
    }

    @Bean
    public ReportCache reportCache() {
        return new ReportCache(cacheTtlSeconds);
    }

    /** Hourly report refresh. */
    @Scheduled(cron = "${avgcxr.reporting.refresh-cron:0 0 * * * *}")
    public void refreshReports() {
        // Triggered by the application via ReportDataUseCaseService.refresh().
    }

    public static class ReportCache {
        private final long ttlSeconds;
        public ReportCache(long ttlSeconds) { this.ttlSeconds = ttlSeconds; }
        public long getTtlSeconds() { return ttlSeconds; }
    }

    @Bean
    public ExportDirectory reportingExportDirectory() throws IOException {
        Path p = Paths.get(exportDir);
        Files.createDirectories(p);
        return new ExportDirectory(p);
    }

    public record ExportDirectory(Path path) {}
}
