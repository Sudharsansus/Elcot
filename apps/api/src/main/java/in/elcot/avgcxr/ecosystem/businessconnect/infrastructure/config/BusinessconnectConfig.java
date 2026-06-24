package in.elcot.avgcxr.ecosystem.businessconnect.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Business connect (company registry) configuration.
 *
 * <p>Provides:</p>
 * <ul>
 *   <li>Verification thresholds (turnover, employee count, GST validation)</li>
 *   <li>Auto-publish on approval (DPDP data retention defaults)</li>
 * </ul>
 */
@Configuration
public class BusinessconnectConfig {

    @Value("${avgcxr.businessconnect.min-turnover-inr:1000000}")
    private long minTurnoverInr;

    @Value("${avgcxr.businessconnect.min-employees:2}")
    private int minEmployees;

    @Value("${avgcxr.businessconnect.auto-publish-on-approval:true}")
    private boolean autoPublishOnApproval;

    @Bean
    public CompanyVerificationThresholds companyVerificationThresholds() {
        return new CompanyVerificationThresholds(minTurnoverInr, minEmployees);
    }

    @Bean
    public CompanyPublishSettings companyPublishSettings() {
        return new CompanyPublishSettings(autoPublishOnApproval);
    }

    public record CompanyVerificationThresholds(long minTurnoverInr, int minEmployees) {}

    public record CompanyPublishSettings(boolean autoPublishOnApproval) {}
}
