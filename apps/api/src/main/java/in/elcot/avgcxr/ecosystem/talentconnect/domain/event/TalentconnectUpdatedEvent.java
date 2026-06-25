package in.elcot.avgcxr.ecosystem.talentconnect.domain.event;

import java.time.Instant;
import java.util.UUID;

public record TalentconnectUpdatedEvent(
    UUID talentconnectId, String field, String oldValue, String newValue, Instant occurredAt) {
  public static TalentconnectUpdatedEvent from(
      UUID id, String field, String oldVal, String newVal) {
    return new TalentconnectUpdatedEvent(id, field, oldVal, newVal, Instant.now());
  }
}
