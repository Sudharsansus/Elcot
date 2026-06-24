package in.elcot.avgcxr.chat.application.service.llm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.http.HttpHeaders;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * OpenAI provider for the AI chat agent.
 *
 * <p>Activated when {@code avgcxr.llm.provider=openai} AND
 * {@code avgcxr.llm.openai.api-key} is set.</p>
 *
 * <p>Default model: gpt-4o-mini (cheap, fast, good enough for portal help).
 * Override with {@code avgcxr.llm.openai.model}.</p>
 */
@Service
@ConditionalOnProperty(name = "avgcxr.llm.provider", havingValue = "openai")
public class OpenAiProvider implements LlmProvider {

    private static final Logger log = LoggerFactory.getLogger(OpenAiProvider.class);

    private final String apiKey;
    private final String model;
    private final String baseUrl;
    private final RestClient http;
    private final ObjectMapper mapper = new ObjectMapper();

    public OpenAiProvider(
            @Value("${avgcxr.llm.openai.api-key:}") String apiKey,
            @Value("${avgcxr.llm.openai.model:gpt-4o-mini}") String model,
            @Value("${avgcxr.llm.openai.base-url:https://api.openai.com/v1}") String baseUrl) {
        this.apiKey = apiKey;
        this.model = model;
        this.baseUrl = baseUrl;
        this.http = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

    @Override
    public String name() { return "openai"; }

    @Override
    public boolean isAvailable() { return apiKey != null && !apiKey.isBlank(); }

    @Override
    public LlmResponse complete(String systemPrompt, List<Map<String, String>> messages, LlmOptions options) {
        long start = System.currentTimeMillis();
        if (!isAvailable()) {
            throw new IllegalStateException("OpenAI API key not configured (avgcxr.llm.openai.api-key)");
        }
        try {
            List<Map<String, Object>> payload = new ArrayList<>();
            if (systemPrompt != null && !systemPrompt.isBlank()) {
                payload.add(Map.of("role", "system", "content", systemPrompt));
            }
            messages.forEach(m -> payload.add(Map.of(
                    "role", m.get("role"),
                    "content", m.get("content")
            )));
            String useModel = options != null && options.model() != null ? options.model() : model;
            Map<String, Object> body = Map.of(
                    "model", useModel,
                    "messages", payload,
                    "temperature", options != null ? options.temperature() : 0.3,
                    "max_tokens", options != null ? options.maxTokens() : 1024
            );

            String responseBody = http.post()
                    .uri("/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .body(body)
                    .retrieve()
                    .body(String.class);

            JsonNode root = mapper.readTree(responseBody);
            String content = root.path("choices").path(0).path("message").path("content").asText("");
            int in = root.path("usage").path("prompt_tokens").asInt(0);
            int out = root.path("usage").path("completion_tokens").asInt(0);
            return LlmResponse.of(content, useModel, in, out, System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("OpenAI request failed: {}", e.getMessage());
            throw new RuntimeException("OpenAI request failed: " + e.getMessage(), e);
        }
    }
}
