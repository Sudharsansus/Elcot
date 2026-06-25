package in.elcot.avgcxr.platform.search.api.rest.dto.response;

import in.elcot.avgcxr.platform.search.domain.model.Search;
import java.time.Instant;

public record SearchResponse(
    String id, String name, String description, Instant createdAt, Instant updatedAt) {
  public static SearchResponse from(Search e) {
    return new SearchResponse(
        e.getId().toString(), "name", "description", e.getCreatedAt(), e.getUpdatedAt());
  }
}
