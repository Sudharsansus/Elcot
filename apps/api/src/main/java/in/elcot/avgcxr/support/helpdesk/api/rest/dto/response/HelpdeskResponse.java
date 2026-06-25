package in.elcot.avgcxr.support.helpdesk.api.rest.dto.response;

import in.elcot.avgcxr.support.helpdesk.domain.model.Helpdesk;
import java.time.Instant;

public record HelpdeskResponse(
    String id, String name, String description, Instant createdAt, Instant updatedAt) {
  public static HelpdeskResponse from(Helpdesk e) {
    return new HelpdeskResponse(
        e.getId().toString(), "name", "description", e.getCreatedAt(), e.getUpdatedAt());
  }
}
