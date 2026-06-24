package in.elcot.avgcxr.chat.infrastructure.persistence.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.elcot.avgcxr.chat.domain.model.ChatMessage;
import in.elcot.avgcxr.chat.domain.model.ChatRole;
import in.elcot.avgcxr.chat.infrastructure.persistence.entity.ChatMessageEntity;

import java.util.List;

public final class ChatMessageMapper {

    private static final ObjectMapper JSON = new ObjectMapper();
    private static final TypeReference<List<String>> STRING_LIST = new TypeReference<>() {};

    private ChatMessageMapper() {}

    public static ChatMessage toDomain(ChatMessageEntity e) {
        if (e == null) return null;
        return ChatMessage.builder()
                .id(e.getId())
                .sessionId(e.getSessionId())
                .role(ChatRole.valueOf(e.getRole()))
                .content(e.getContent())
                .contentTamil(e.getContentTamil())
                .language(e.getLanguage())
                .modelUsed(e.getModelUsed())
                .tokensUsed(e.getTokensUsed())
                .latencyMs(e.getLatencyMs())
                .ragContextIds(readJsonArray(e.getRagContextIds()))
                .createdAt(e.getCreatedAt())
                .build();
    }

    public static ChatMessageEntity toEntity(ChatMessage m) {
        if (m == null) return null;
        return ChatMessageEntity.builder()
                .id(m.getId())
                .sessionId(m.getSessionId())
                .role(m.getRole() == null ? null : m.getRole().name())
                .content(m.getContent())
                .contentTamil(m.getContentTamil())
                .language(m.getLanguage())
                .modelUsed(m.getModelUsed())
                .tokensUsed(m.getTokensUsed())
                .latencyMs(m.getLatencyMs())
                .ragContextIds(writeJsonArray(m.getRagContextIds()))
                .createdAt(m.getCreatedAt())
                .build();
    }

    /** Serialize the RAG source-id list to a JSON array string for the jsonb column. */
    private static String writeJsonArray(List<String> list) {
        if (list == null || list.isEmpty()) return "[]";
        try {
            return JSON.writeValueAsString(list);
        } catch (Exception e) {
            return "[]";
        }
    }

    /** Parse a JSON array string (jsonb column) back into a list of RAG source ids. */
    private static List<String> readJsonArray(String json) {
        if (json == null || json.isBlank()) return List.of();
        try {
            return JSON.readValue(json, STRING_LIST);
        } catch (Exception e) {
            return List.of();
        }
    }
}
