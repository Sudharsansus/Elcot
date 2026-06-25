package in.elcot.avgcxr.analytics.reporting.domain.event;

import in.elcot.avgcxr.platformevents.domain.DomainEvent;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class ReportDataCreatedEvent extends DomainEvent {
  public ReportDataCreatedEvent(UUID entityId) {
    super(
        UUID.randomUUID(),
        "REPORTDATA_CREATED",
        LocalDateTime.now(),
        entityId.toString(),
        Map.of("entityId", entityId.toString()));
  }
}
