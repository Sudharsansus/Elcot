package in.elcot.avgcxr.chat.infrastructure.persistence.adapter;

import in.elcot.avgcxr.chat.application.port.output.ChatSessionRepositoryPort;
import in.elcot.avgcxr.chat.domain.model.ChatSession;
import in.elcot.avgcxr.chat.infrastructure.persistence.entity.ChatSessionEntity;
import in.elcot.avgcxr.chat.infrastructure.persistence.mapper.ChatSessionMapper;
import in.elcot.avgcxr.chat.infrastructure.persistence.repository.JpaChatSessionRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ChatSessionRepositoryAdapter implements ChatSessionRepositoryPort {

    private final JpaChatSessionRepository jpaRepo;

    public ChatSessionRepositoryAdapter(JpaChatSessionRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public ChatSession save(ChatSession session) {
        ChatSessionEntity entity = ChatSessionMapper.toEntity(session);
        return ChatSessionMapper.toDomain(jpaRepo.save(entity));
    }

    @Override
    public Optional<ChatSession> findById(UUID id) {
        return jpaRepo.findById(id).map(ChatSessionMapper::toDomain);
    }

    @Override
    public Optional<ChatSession> findBySessionToken(String token) {
        return jpaRepo.findBySessionToken(token).map(ChatSessionMapper::toDomain);
    }

    @Override
    public List<ChatSession> findByUserId(UUID userId) {
        return jpaRepo.findByUserId(userId).stream().map(ChatSessionMapper::toDomain).toList();
    }

    @Override
    public int deleteExpired() {
        return jpaRepo.deleteExpired(Instant.now());
    }
}
