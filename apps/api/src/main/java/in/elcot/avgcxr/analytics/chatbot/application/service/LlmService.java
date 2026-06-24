package in.elcot.avgcxr.analytics.chatbot.application.service;

import java.util.List;
import java.util.Map;

/**
 * LLM service abstraction (RAG-ready).
 *
 * <p>The Mira chatbot can call any implementation of this interface to
 * generate a response. The default implementation ({@link RuleBasedLlmService})
 * uses pattern matching as a fallback when no LLM is configured. The
 * RAG-aware implementation queries the scheme catalog for context
 * before composing a response.</p>
 *
 * <p>To plug in a real LLM (e.g., OpenAI, Anthropic, or a local model),
 * provide a Spring bean implementing this interface and exclude
 * {@link RuleBasedLlmService} from auto-wiring. The Mira service picks
 * up whichever bean is present.</p>
 */
public interface LlmService {

    /**
     * Generate a response to the user's message, optionally grounded
     * by the given retrieved context (RAG).
     *
     * @param userMessage the user's input (English or Tamil)
     * @param context     retrieved documents/schemes/etc. that ground the answer
     * @param lang        "en" or "ta"
     * @return a response map: { "reply": String, "sources": List<String>, "model": String }
     */
    Map<String, Object> generate(String userMessage, List<Map<String, Object>> context, String lang);
}
