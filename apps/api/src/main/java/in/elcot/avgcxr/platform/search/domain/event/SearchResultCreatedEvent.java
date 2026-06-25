package in.elcot.avgcxr.platform.search.domain.event;

import in.elcot.avgcxr.platformevents.domain.DomainEvent;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class SearchResultCreatedEvent extends DomainEvent {
  public SearchResultCreatedEvent(UUID entityId) {
    super(
        UUID.randomUUID(),
        "SEARCHRESULT_CREATED",
        LocalDateTime.now(),
        entityId.toString(),
        Map.of("entityId", entityId.toString()));
  }
}
