package in.elcot.avgcxr.platform.search.infrastructure.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Search service configuration.
 *
 * <p>Provides:
 *
 * <ul>
 *   <li>Indexed entity list (scheme, company, talent, freelancer, application)
 *   <li>Default page size and max results
 *   <li>Tamil full-text search support toggle
 * </ul>
 */
@Configuration
public class SearchConfig {

  @Value(
      "${avgcxr.search.indexed-entities:scheme,company,talent,freelancer,application,grievance,helpdesk}")
  private String indexedEntities;

  @Value("${avgcxr.search.default-page-size:20}")
  private int defaultPageSize;

  @Value("${avgcxr.search.max-page-size:100}")
  private int maxPageSize;

  @Value("${avgcxr.search.tamil-support:true}")
  private boolean tamilSupport;

  @Bean
  public SearchSettings searchSettings() {
    return new SearchSettings(
        List.of(indexedEntities.split(",")).stream()
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .toList(),
        defaultPageSize,
        maxPageSize,
        tamilSupport);
  }

  public record SearchSettings(
      List<String> indexedEntities, int defaultPageSize, int maxPageSize, boolean tamilSupport) {}
}
