package in.elcot.avgcxr.chat.application.port.output;

import in.elcot.avgcxr.chat.domain.model.ChatSession;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Output port for chat session persistence. Implemented by infrastructure adapter. */
public interface ChatSessionRepositoryPort {
  ChatSession save(ChatSession session);

  Optional<ChatSession> findById(UUID id);

  Optional<ChatSession> findBySessionToken(String token);

  List<ChatSession> findByUserId(UUID userId);

  int deleteExpired();
}
