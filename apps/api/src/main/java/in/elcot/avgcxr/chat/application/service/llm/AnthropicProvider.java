package in.elcot.avgcxr.chat.application.service.llm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * Anthropic Claude provider for the AI chat agent.
 *
 * <p>Activated when {@code avgcxr.llm.provider=anthropic} AND {@code avgcxr.llm.anthropic.api-key}
 * is set.
 *
 * <p>Default model: claude-3-5-haiku-latest (cheap, fast, 200K context). Override with {@code
 * avgcxr.llm.anthropic.model}.
 */
@Service
@ConditionalOnProperty(name = "avgcxr.llm.provider", havingValue = "anthropic")
public class AnthropicProvider implements LlmProvider {

  private static final Logger log = LoggerFactory.getLogger(AnthropicProvider.class);

  private final String apiKey;
  private final String model;
  private final String baseUrl;
  private final RestClient http;
  private final ObjectMapper mapper = new ObjectMapper();

  public AnthropicProvider(
      @Value("${avgcxr.llm.anthropic.api-key:}") String apiKey,
      @Value("${avgcxr.llm.anthropic.model:claude-3-5-haiku-latest}") String model,
      @Value("${avgcxr.llm.anthropic.base-url:https://api.anthropic.com/v1}") String baseUrl) {
    this.apiKey = apiKey;
    this.model = model;
    this.baseUrl = baseUrl;
    this.http =
        RestClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .build();
  }

  @Override
  public String name() {
    return "anthropic";
  }

  @Override
  public boolean isAvailable() {
    return apiKey != null && !apiKey.isBlank();
  }

  @Override
  public LlmResponse complete(
      String systemPrompt, List<Map<String, String>> messages, LlmOptions options) {
    long start = System.currentTimeMillis();
    if (!isAvailable()) {
      throw new IllegalStateException("Anthropic API key not configured");
    }
    try {
      // Anthropic uses separate "system" field; messages only have user/assistant
      List<Map<String, Object>> anthropicMsgs = new ArrayList<>();
      for (Map<String, String> m : messages) {
        String role = m.get("role");
        if ("system".equals(role)) continue; // skip; passed in system field
        if (role == null) role = "user";
        if (!"user".equals(role) && !"assistant".equals(role)) role = "user";
        anthropicMsgs.add(Map.of("role", role, "content", m.get("content")));
      }
      String useModel = options != null && options.model() != null ? options.model() : model;
      Map<String, Object> body = new java.util.LinkedHashMap<>();
      body.put("model", useModel);
      body.put("max_tokens", options != null ? options.maxTokens() : 1024);
      if (systemPrompt != null && !systemPrompt.isBlank()) {
        body.put("system", systemPrompt);
      }
      body.put("messages", anthropicMsgs);
      body.put("temperature", options != null ? options.temperature() : 0.3);

      String responseBody =
          http.post()
              .uri("/messages")
              .header("x-api-key", apiKey)
              .header("anthropic-version", "2023-06-01")
              .body(body)
              .retrieve()
              .body(String.class);

      JsonNode root = mapper.readTree(responseBody);
      // Anthropic returns content as array of blocks
      StringBuilder content = new StringBuilder();
      JsonNode contentArr = root.path("content");
      if (contentArr.isArray()) {
        for (JsonNode block : contentArr) {
          if ("text".equals(block.path("type").asText())) {
            content.append(block.path("text").asText());
          }
        }
      }
      int in = root.path("usage").path("input_tokens").asInt(0);
      int out = root.path("usage").path("output_tokens").asInt(0);
      return LlmResponse.of(
          content.toString(), useModel, in, out, System.currentTimeMillis() - start);
    } catch (Exception e) {
      log.error("Anthropic request failed: {}", e.getMessage());
      throw new RuntimeException("Anthropic request failed: " + e.getMessage(), e);
    }
  }
}
