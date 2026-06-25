package in.elcot.avgcxr.ecosystem.talentconnect.domain.event;

import java.time.Instant;
import java.util.UUID;

public record TalentconnectCreatedEvent(UUID talentconnectId, String action, Instant occurredAt) {
  public static TalentconnectCreatedEvent from(UUID id) {
    return new TalentconnectCreatedEvent(id, "CREATED", Instant.now());
  }
}
