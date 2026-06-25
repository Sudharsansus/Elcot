package in.elcot.avgcxr.ecosystem.talentconnect.api.rest.dto.response;

import in.elcot.avgcxr.ecosystem.talentconnect.domain.model.Talentconnect;
import java.time.Instant;

public record TalentconnectResponse(
    String id, String name, String description, Instant createdAt, Instant updatedAt) {
  public static TalentconnectResponse from(Talentconnect e) {
    return new TalentconnectResponse(
        e.getId().toString(), "name", "description", e.getCreatedAt(), e.getUpdatedAt());
  }
}
