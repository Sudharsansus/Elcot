package in.elcot.avgcxr.platform.user.domain.event;

import in.elcot.avgcxr.platformevents.domain.DomainEvent;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class UserProfileCreatedEvent extends DomainEvent {
  public UserProfileCreatedEvent(UUID entityId) {
    super(
        UUID.randomUUID(),
        "USERPROFILE_CREATED",
        LocalDateTime.now(),
        entityId.toString(),
        Map.of("entityId", entityId.toString()));
  }
}
