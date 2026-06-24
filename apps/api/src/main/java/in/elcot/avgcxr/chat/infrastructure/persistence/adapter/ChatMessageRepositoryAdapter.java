package in.elcot.avgcxr.chat.infrastructure.persistence.adapter;

import in.elcot.avgcxr.chat.application.port.output.ChatMessageRepositoryPort;
import in.elcot.avgcxr.chat.domain.model.ChatMessage;
import in.elcot.avgcxr.chat.infrastructure.persistence.entity.ChatMessageEntity;
import in.elcot.avgcxr.chat.infrastructure.persistence.mapper.ChatMessageMapper;
import in.elcot.avgcxr.chat.infrastructure.persistence.repository.JpaChatMessageRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public class ChatMessageRepositoryAdapter implements ChatMessageRepositoryPort {

    private final JpaChatMessageRepository jpaRepo;

    public ChatMessageRepositoryAdapter(JpaChatMessageRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public ChatMessage save(ChatMessage message) {
        ChatMessageEntity entity = ChatMessageMapper.toEntity(message);
        return ChatMessageMapper.toDomain(jpaRepo.save(entity));
    }

    @Override
    public List<ChatMessage> findBySessionId(UUID sessionId) {
        return jpaRepo.findBySessionId(sessionId).stream().map(ChatMessageMapper::toDomain).toList();
    }

    @Override
    public List<ChatMessage> findRecentBySessionId(UUID sessionId, int limit) {
        return jpaRepo.findRecentBySessionId(sessionId, limit).stream().map(ChatMessageMapper::toDomain).toList();
    }

    @Override
    public long countBySessionId(UUID sessionId) {
        return jpaRepo.countBySessionId(sessionId);
    }

    @Override
    @Transactional
    public void deleteBySessionId(UUID sessionId) {
        List<ChatMessageEntity> msgs = jpaRepo.findBySessionId(sessionId);
        jpaRepo.deleteAll(msgs);
    }
}
