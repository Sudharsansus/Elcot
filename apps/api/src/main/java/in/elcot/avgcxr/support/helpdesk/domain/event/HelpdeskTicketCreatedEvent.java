package in.elcot.avgcxr.support.helpdesk.domain.event;

import in.elcot.avgcxr.platformevents.domain.DomainEvent;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class HelpdeskTicketCreatedEvent extends DomainEvent {
    public HelpdeskTicketCreatedEvent(UUID entityId) {
        super(UUID.randomUUID(), "HELPDESKTICKET_CREATED", LocalDateTime.now(), entityId.toString(),
                Map.of("entityId", entityId.toString()));
    }
}
