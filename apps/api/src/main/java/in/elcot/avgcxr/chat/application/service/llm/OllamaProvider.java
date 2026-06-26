package in.elcot.avgcxr.chat.application.service.llm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * Ollama (local LLM) provider for the AI chat agent.
 *
 * <p>Activated when {@code avgcxr.llm.provider=ollama}. Useful for air-gapped deployments or when
 * no API key is available.
 *
 * <p>Default model: llama3.1:8b. Override with {@code avgcxr.llm.ollama.model=qwen2.5:7b} (Qwen has
 * good Tamil support) or {@code avgcxr.llm.ollama.model=mistral:7b}.
 */
@Service
@ConditionalOnProperty(name = "avgcxr.llm.provider", havingValue = "ollama")
public class OllamaProvider implements LlmProvider {

  private static final Logger log = LoggerFactory.getLogger(OllamaProvider.class);

  private final String baseUrl;
  private final String model;
  private final RestClient http;
  private final ObjectMapper mapper = new ObjectMapper();

  public OllamaProvider(
      @Value("${avgcxr.llm.ollama.base-url:http://localhost:11434}") String baseUrl,
      @Value("${avgcxr.llm.ollama.model:llama3.1:8b}") String model) {
    this.baseUrl = baseUrl;
    this.model = model;
    // Fail fast if the (self-hosted) Ollama host is unreachable or slow, so the
    // request degrades to the ChatService fallback instead of hanging the thread.
    SimpleClientHttpRequestFactory rf = new SimpleClientHttpRequestFactory();
    rf.setConnectTimeout((int) Duration.ofSeconds(5).toMillis());
    rf.setReadTimeout((int) Duration.ofSeconds(120).toMillis());
    this.http =
        RestClient.builder()
            .baseUrl(baseUrl)
            .requestFactory(rf)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .build();
  }

  @Override
  public String name() {
    return "ollama";
  }

  @Override
  public boolean isAvailable() {
    try {
      http.get().uri("/api/tags").retrieve().toBodilessEntity();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public LlmResponse complete(
      String systemPrompt, List<Map<String, String>> messages, LlmOptions options) {
    long start = System.currentTimeMillis();
    try {
      String useModel = options != null && options.model() != null ? options.model() : model;
      // Ollama /api/chat expects a single messages array with optional system message
      java.util.List<Map<String, Object>> msgs = new java.util.ArrayList<>();
      if (systemPrompt != null && !systemPrompt.isBlank()) {
        msgs.add(Map.of("role", "system", "content", systemPrompt));
      }
      messages.forEach(
          m -> {
            String role = m.get("role");
            String content = m.get("content");
            msgs.add(
                Map.of(
                    "role", role == null ? "user" : role,
                    "content", content == null ? "" : content));
          });
      Map<String, Object> body =
          Map.of(
              "model",
              useModel,
              "messages",
              msgs,
              "stream",
              false,
              "options",
              Map.of(
                  "temperature", options != null ? options.temperature() : 0.3,
                  "num_predict", options != null ? options.maxTokens() : 1024));

      String responseBody = http.post().uri("/api/chat").body(body).retrieve().body(String.class);

      JsonNode root = mapper.readTree(responseBody);
      String content = root.path("message").path("content").asText("");
      int in = root.path("prompt_eval_count").asInt(0);
      int out = root.path("eval_count").asInt(0);
      return LlmResponse.of(content, useModel, in, out, System.currentTimeMillis() - start);
    } catch (Exception e) {
      log.error("Ollama request failed: {}", e.getMessage());
      throw new RuntimeException("Ollama request failed: " + e.getMessage(), e);
    }
  }
}
