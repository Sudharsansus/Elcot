package in.elcot.avgcxr.chat.application.port.output;

import in.elcot.avgcxr.chat.domain.model.ChatMessage;

import java.util.List;
import java.util.UUID;

/**
 * Output port for chat message persistence.
 */
public interface ChatMessageRepositoryPort {
    ChatMessage save(ChatMessage message);
    List<ChatMessage> findBySessionId(UUID sessionId);
    List<ChatMessage> findRecentBySessionId(UUID sessionId, int limit);
    long countBySessionId(UUID sessionId);
    void deleteBySessionId(UUID sessionId);
}
