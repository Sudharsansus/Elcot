package in.elcot.avgcxr.ecosystem.businessconnect.domain.event;

import in.elcot.avgcxr.platformevents.domain.DomainEvent;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class CompanyCreatedEvent extends DomainEvent {
  public CompanyCreatedEvent(UUID entityId) {
    super(
        UUID.randomUUID(),
        "COMPANY_CREATED",
        LocalDateTime.now(),
        entityId.toString(),
        Map.of("entityId", entityId.toString()));
  }
}
