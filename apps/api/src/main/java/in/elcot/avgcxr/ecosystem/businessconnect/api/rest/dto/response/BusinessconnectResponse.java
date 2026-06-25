package in.elcot.avgcxr.ecosystem.businessconnect.api.rest.dto.response;

import in.elcot.avgcxr.ecosystem.businessconnect.domain.model.Businessconnect;
import java.time.Instant;

public record BusinessconnectResponse(
    String id, String name, String description, Instant createdAt, Instant updatedAt) {
  public static BusinessconnectResponse from(Businessconnect e) {
    return new BusinessconnectResponse(
        e.getId().toString(), "name", "description", e.getCreatedAt(), e.getUpdatedAt());
  }
}
