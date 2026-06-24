package in.elcot.avgcxr.ecosystem.talentconnect.domain.event;

import in.elcot.avgcxr.platformevents.domain.DomainEvent;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class TalentProfileCreatedEvent extends DomainEvent {
    public TalentProfileCreatedEvent(UUID entityId) {
        super(UUID.randomUUID(), "TALENTPROFILE_CREATED", LocalDateTime.now(), entityId.toString(),
                Map.of("entityId", entityId.toString()));
    }
}
