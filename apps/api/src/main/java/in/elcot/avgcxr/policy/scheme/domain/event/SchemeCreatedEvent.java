package in.elcot.avgcxr.policy.scheme.domain.event;

import in.elcot.avgcxr.platformevents.domain.DomainEvent;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class SchemeCreatedEvent extends DomainEvent {
  public SchemeCreatedEvent(UUID schemeId, String name, String category) {
    super(
        UUID.randomUUID(),
        "SCHEME_CREATED",
        LocalDateTime.now(),
        schemeId.toString(),
        Map.of("schemeId", schemeId.toString(), "name", name, "category", category));
  }
}
