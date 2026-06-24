package in.elcot.avgcxr.platform.notification.domain.event;

import in.elcot.avgcxr.platformevents.domain.DomainEvent;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class NotificationCreatedEvent extends DomainEvent {
    public NotificationCreatedEvent(UUID entityId) {
        super(UUID.randomUUID(), "NOTIFICATION_CREATED", LocalDateTime.now(), entityId.toString(),
                Map.of("entityId", entityId.toString()));
    }
}
