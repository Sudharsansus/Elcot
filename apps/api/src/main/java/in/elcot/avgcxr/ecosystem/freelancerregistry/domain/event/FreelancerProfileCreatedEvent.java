package in.elcot.avgcxr.ecosystem.freelancerregistry.domain.event;

import in.elcot.avgcxr.platformevents.domain.DomainEvent;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class FreelancerProfileCreatedEvent extends DomainEvent {
  public FreelancerProfileCreatedEvent(UUID entityId) {
    super(
        UUID.randomUUID(),
        "FREELANCERPROFILE_CREATED",
        LocalDateTime.now(),
        entityId.toString(),
        Map.of("entityId", entityId.toString()));
  }
}
