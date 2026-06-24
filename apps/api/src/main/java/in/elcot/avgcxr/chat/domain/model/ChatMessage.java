package in.elcot.avgcxr.chat.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Pure domain representation of a single chat message.
 * No framework imports — persistence is handled by infrastructure.entity + mapper.
 */
@Getter
@Builder
public class ChatMessage {

    private UUID id;
    private UUID sessionId;
    private ChatRole role;
    private String content;
    private String contentTamil;     // pre-translated Tamil version (if available)
    private String language;         // 'en' or 'ta'
    private String modelUsed;        // which LLM model generated this (null for USER msgs)
    private Integer tokensUsed;
    private Integer latencyMs;
    private List<String> ragContextIds;  // retrieved document IDs
    private Map<String, Object> metadata;
    private Instant createdAt;

    public static ChatMessage userMessage(UUID sessionId, String content, String language) {
        return ChatMessage.builder()
                .id(UUID.randomUUID())
                .sessionId(sessionId)
                .role(ChatRole.USER)
                .content(content)
                .language(language)
                .createdAt(Instant.now())
                .build();
    }

    public static ChatMessage assistantMessage(UUID sessionId, String content, String language,
                                                String model, int tokens, int latencyMs,
                                                List<String> ragIds) {
        return ChatMessage.builder()
                .id(UUID.randomUUID())
                .sessionId(sessionId)
                .role(ChatRole.ASSISTANT)
                .content(content)
                .language(language)
                .modelUsed(model)
                .tokensUsed(tokens)
                .latencyMs(latencyMs)
                .ragContextIds(ragIds)
                .createdAt(Instant.now())
                .build();
    }
}
