package in.elcot.avgcxr.ecosystem.freelancerregistry.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Freelancer registry configuration.
 *
 * <p>Provides:
 *
 * <ul>
 *   <li>Skill taxonomy settings (max skills, categories)
 *   <li>Hourly-rate validation bounds
 *   <li>Availability-tracking defaults
 * </ul>
 */
@Configuration
public class FreelancerregistryConfig {

  @Value("${avgcxr.freelancer.max-skills:20}")
  private int maxSkills;

  @Value("${avgcxr.freelancer.min-hourly-rate-inr:100}")
  private long minHourlyRate;

  @Value("${avgcxr.freelancer.max-hourly-rate-inr:50000}")
  private long maxHourlyRate;

  @Bean
  public FreelancerLimits freelancerLimits() {
    return new FreelancerLimits(maxSkills, minHourlyRate, maxHourlyRate);
  }

  public record FreelancerLimits(int maxSkills, long minHourlyRate, long maxHourlyRate) {}
}
