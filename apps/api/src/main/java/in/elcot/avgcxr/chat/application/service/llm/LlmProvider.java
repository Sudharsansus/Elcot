package in.elcot.avgcxr.chat.application.service.llm;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * LLM provider abstraction. Implementations:
 *  - OpenAiProvider (GPT-4o, GPT-4o-mini, GPT-3.5-turbo)
 *  - AnthropicProvider (Claude Haiku, Sonnet, Opus)
 *  - OllamaProvider (local models: Llama 3, Mistral, Qwen)
 *  - RuleBasedLlmProvider (fallback when no API key configured)
 *
 * <p>Switch via configuration: {@code avgcxr.llm.provider=openai|anthropic|ollama|rule-based}.</p>
 */
public interface LlmProvider {

    /** Display name of the provider. */
    String name();

    /** Whether the provider is currently available (API key configured, model loaded, etc). */
    boolean isAvailable();

    /**
     * Generate a non-streaming response.
     *
     * @param systemPrompt  the system message
     * @param messages      the conversation history (each item: {role, content})
     * @param options       temperature, maxTokens, etc.
     * @return the assistant's reply text plus usage info
     */
    LlmResponse complete(String systemPrompt, List<Map<String, String>> messages, LlmOptions options);

    /**
     * Streaming variant. Calls {@code onChunk} for each token; completes with full text.
     * Default impl falls back to non-streaming complete().
     */
    default void streamComplete(String systemPrompt,
                                List<Map<String, String>> messages,
                                LlmOptions options,
                                Consumer<String> onChunk,
                                Runnable onComplete) {
        LlmResponse r = complete(systemPrompt, messages, options);
        onChunk.accept(r.content());
        onComplete.run();
    }

    record LlmOptions(double temperature, int maxTokens, String model) {
        public static LlmOptions defaults() {
            return new LlmOptions(0.3, 1024, null);
        }
    }

    record LlmResponse(String content, String model, int inputTokens, int outputTokens,
                       int totalTokens, long latencyMs) {
        public static LlmResponse of(String content, String model, int in, int out, long ms) {
            return new LlmResponse(content, model, in, out, in + out, ms);
        }
    }
}
