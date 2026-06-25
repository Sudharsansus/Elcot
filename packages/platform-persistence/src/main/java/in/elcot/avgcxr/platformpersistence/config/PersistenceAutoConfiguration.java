package in.elcot.avgcxr.platformpersistence.config;

import java.util.Optional;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Auto-configuration for JPA persistence layer. Enables JPA auditing, entity scanning, and
 * repository configuration.
 *
 * <p>All bounded contexts benefit from this auto-configuration, which sets up createdAt/updatedAt
 * audit columns and soft delete support automatically.
 */
@AutoConfiguration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableJpaRepositories(basePackages = "in.elcot.avgcxr")
@EntityScan(basePackages = "in.elcot.avgcxr")
public class PersistenceAutoConfiguration {

  /**
   * Provides the current auditor (user ID) for JPA auditing. Reads the user ID from the security
   * context or falls back to "system".
   *
   * @return AuditorAware that resolves the current authenticated user
   */
  @Bean
  @ConditionalOnMissingBean
  public AuditorAware<String> auditorAware() {
    return () -> {
      try {
        var securityContext =
            org.springframework.security.core.context.SecurityContextHolder.getContext();
        var authentication = securityContext.getAuthentication();
        if (authentication != null
            && authentication.isAuthenticated()
            && !"anonymousUser".equals(authentication.getPrincipal())) {
          return Optional.of(authentication.getName());
        }
      } catch (Exception e) {
        // Security context not available
      }
      return Optional.of("system");
    };
  }
}
