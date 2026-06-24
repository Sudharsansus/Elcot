package in.elcot.avgcxr.chat;

import in.elcot.avgcxr.chat.domain.model.ChatMessage;
import in.elcot.avgcxr.chat.domain.model.ChatRole;
import in.elcot.avgcxr.chat.domain.model.ChatSession;
import in.elcot.avgcxr.chat.infrastructure.persistence.entity.ChatMessageEntity;
import in.elcot.avgcxr.chat.infrastructure.persistence.entity.ChatSessionEntity;
import in.elcot.avgcxr.chat.infrastructure.persistence.mapper.ChatMessageMapper;
import in.elcot.avgcxr.chat.infrastructure.persistence.mapper.ChatSessionMapper;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ChatMappersTest {

    @Test
    void chatSessionRoundTrip() {
        ChatSession domain = ChatSession.newAnonymous("en");
        domain.setTitle("hello");
        ChatSessionEntity entity = ChatSessionMapper.toEntity(domain);
        assertNotNull(entity);
        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getTitle(), entity.getTitle());

        ChatSession roundTrip = ChatSessionMapper.toDomain(entity);
        assertEquals(domain.getId(), roundTrip.getId());
        assertEquals(domain.getTitle(), roundTrip.getTitle());
        assertEquals(domain.getLanguage(), roundTrip.getLanguage());
    }

    @Test
    void chatMessageRoundTrip() {
        UUID sessionId = UUID.randomUUID();
        ChatMessage domain = ChatMessage.assistantMessage(
                sessionId, "Hello", "en", "gpt-4o-mini", 42, 123, java.util.List.of());
        ChatMessageEntity entity = ChatMessageMapper.toEntity(domain);
        assertNotNull(entity);
        assertEquals(domain.getId(), entity.getId());
        assertEquals(ChatRole.ASSISTANT.name(), entity.getRole());

        ChatMessage roundTrip = ChatMessageMapper.toDomain(entity);
        assertEquals(domain.getId(), roundTrip.getId());
        assertEquals(domain.getContent(), roundTrip.getContent());
        assertEquals(ChatRole.ASSISTANT, roundTrip.getRole());
    }

    @Test
    void ragContextIdsRoundTrip() {
        UUID sessionId = UUID.randomUUID();
        var ragIds = java.util.List.of("doc-1", "scheme-42", "faq-7");
        ChatMessage domain = ChatMessage.assistantMessage(
                sessionId, "Answer", "ta", "gpt-4o-mini", 10, 50, ragIds);

        ChatMessageEntity entity = ChatMessageMapper.toEntity(domain);
        // The list must be serialized to a real JSON array (regression: it used to always be "[]").
        assertEquals("[\"doc-1\",\"scheme-42\",\"faq-7\"]", entity.getRagContextIds());

        ChatMessage roundTrip = ChatMessageMapper.toDomain(entity);
        assertEquals(ragIds, roundTrip.getRagContextIds());
    }
}
