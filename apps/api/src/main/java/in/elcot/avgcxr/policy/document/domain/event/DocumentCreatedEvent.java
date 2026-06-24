package in.elcot.avgcxr.policy.document.domain.event;

import in.elcot.avgcxr.platformevents.domain.DomainEvent;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class DocumentCreatedEvent extends DomainEvent {
    public DocumentCreatedEvent(UUID entityId) {
        super(UUID.randomUUID(), "DOCUMENT_CREATED", LocalDateTime.now(), entityId.toString(),
                Map.of("entityId", entityId.toString()));
    }
}
