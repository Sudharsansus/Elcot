package in.elcot.avgcxr.chat.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Chat agent configuration.
 *
 * <p>LLM provider selection (in priority order, first available wins):
 *
 * <ol>
 *   <li>{@code avgcxr.llm.provider=openai} -- needs {@code avgcxr.llm.openai.api-key}
 *   <li>{@code avgcxr.llm.provider=anthropic} -- needs {@code avgcxr.llm.anthropic.api-key}
 *   <li>{@code avgcxr.llm.provider=ollama} -- needs Ollama running locally
 *   <li>unset or {@code rule-based} -- fallback (always available)
 * </ol>
 */
@Configuration
@EnableScheduling
public class ChatConfig {

  @Value("${avgcxr.llm.provider:rule-based}")
  private String defaultProvider;

  @Value("${avgcxr.llm.rag.top-k:5}")
  private int ragTopK;

  @Value("${avgcxr.llm.rag.min-score:0.3}")
  private double ragMinScore;

  @Value("${avgcxr.llm.session.retention-days:90}")
  private int retentionDays;

  @Bean
  public ChatSettings chatSettings() {
    return new ChatSettings(defaultProvider, ragTopK, ragMinScore, retentionDays);
  }

  // The daily session purge is scheduled in ChatService.purgeExpired()
  // (where the session repository is already injected). @EnableScheduling above activates it.

  public record ChatSettings(
      String defaultProvider, int ragTopK, double ragMinScore, int retentionDays) {}
}
