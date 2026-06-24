package in.elcot.avgcxr.chat;

import in.elcot.avgcxr.chat.application.port.output.ChatMessageRepositoryPort;
import in.elcot.avgcxr.chat.application.port.output.ChatSessionRepositoryPort;
import in.elcot.avgcxr.chat.application.service.ChatService;
import in.elcot.avgcxr.chat.application.service.PromptBuilder;
import in.elcot.avgcxr.chat.application.service.RagService;
import in.elcot.avgcxr.chat.application.service.TamilSupport;
import in.elcot.avgcxr.chat.application.service.llm.LlmProvider;
import in.elcot.avgcxr.chat.application.service.llm.RuleBasedLlmProvider;
import in.elcot.avgcxr.chat.domain.model.ChatMessage;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Service-layer test for the chat agent.
 *
 * <p>Uses Mockito-mocked repository ports (the project has no embedded test DB),
 * with the REAL {@link RuleBasedLlmProvider}, {@link PromptBuilder} and
 * {@link TamilSupport} so the bilingual orchestration is genuinely exercised.</p>
 */
class ChatServiceTest {

    private ChatService newService(ChatSessionRepositoryPort sessionRepo,
                                   ChatMessageRepositoryPort messageRepo) {
        RagService rag = mock(RagService.class);
        when(rag.retrieve(anyString(), anyString())).thenReturn(List.of());
        return new ChatService(new RuleBasedLlmProvider(), rag, sessionRepo, messageRepo,
                new PromptBuilder(), new TamilSupport());
    }

    @Test
    void sendMessageCreatesSessionPersistsBothMessagesAndReplies() {
        ChatSessionRepositoryPort sessionRepo = mock(ChatSessionRepositoryPort.class);
        ChatMessageRepositoryPort messageRepo = mock(ChatMessageRepositoryPort.class);
        when(sessionRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        when(messageRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        // History is what the rule-based provider matches against:
        when(messageRepo.findRecentBySessionId(any(), anyInt())).thenReturn(
                List.of(ChatMessage.userMessage(UUID.randomUUID(), "What schemes are available?", "en")));

        ChatService svc = newService(sessionRepo, messageRepo);
        ChatService.ChatTurnResult result =
                svc.sendMessage(null, null, "What schemes are available?", "en");

        assertNotNull(result);
        assertNotNull(result.assistantMessage());
        assertFalse(result.assistantMessage().getContent().isBlank(),
                "assistant reply must not be blank");
        // user + assistant message both persisted
        verify(messageRepo, times(2)).save(any());
        verify(sessionRepo, atLeastOnce()).save(any());
    }

    @Test
    void ruleBasedProviderAnswersEnglishQuery() {
        LlmProvider p = new RuleBasedLlmProvider();
        LlmProvider.LlmResponse r = p.complete("sys",
                List.of(PromptBuilder.msg("user", "What schemes are available?")),
                LlmProvider.LlmOptions.defaults());
        assertNotNull(r);
        assertFalse(r.content().isBlank());
        assertTrue(r.content().toLowerCase().contains("scheme"),
                "the scheme-rule answer should mention schemes");
    }

    @Test
    void ruleBasedProviderRepliesWithTamilScript() {
        // "tamil" hits the Tamil/language rule (rule 9), whose answer contains தமிழ்.
        LlmProvider p = new RuleBasedLlmProvider();
        LlmProvider.LlmResponse r = p.complete("sys",
                List.of(PromptBuilder.msg("user", "tamil")),
                LlmProvider.LlmOptions.defaults());
        assertTrue(r.content().codePoints().anyMatch(cp -> cp >= 0x0B80 && cp <= 0x0BFF),
                "reply must contain Tamil script (U+0B80-U+0BFF)");
    }

    @Test
    void purgeExpiredDelegatesToRepository() {
        ChatSessionRepositoryPort sessionRepo = mock(ChatSessionRepositoryPort.class);
        ChatMessageRepositoryPort messageRepo = mock(ChatMessageRepositoryPort.class);
        when(sessionRepo.deleteExpired()).thenReturn(7);

        ChatService svc = newService(sessionRepo, messageRepo);

        assertEquals(7, svc.purgeExpired());
        verify(sessionRepo).deleteExpired();
    }
}
