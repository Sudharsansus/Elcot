package in.elcot.avgcxr.support.grievance.domain.event;

import in.elcot.avgcxr.platformevents.domain.DomainEvent;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class GrievanceCreatedEvent extends DomainEvent {
    public GrievanceCreatedEvent(UUID entityId) {
        super(UUID.randomUUID(), "GRIEVANCE_CREATED", LocalDateTime.now(), entityId.toString(),
                Map.of("entityId", entityId.toString()));
    }
}
