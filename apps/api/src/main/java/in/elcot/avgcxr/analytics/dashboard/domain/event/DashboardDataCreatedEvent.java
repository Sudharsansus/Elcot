package in.elcot.avgcxr.analytics.dashboard.domain.event;

import in.elcot.avgcxr.platformevents.domain.DomainEvent;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class DashboardDataCreatedEvent extends DomainEvent {
    public DashboardDataCreatedEvent(UUID entityId) {
        super(UUID.randomUUID(), "DASHBOARDDATA_CREATED", LocalDateTime.now(), entityId.toString(),
                Map.of("entityId", entityId.toString()));
    }
}
