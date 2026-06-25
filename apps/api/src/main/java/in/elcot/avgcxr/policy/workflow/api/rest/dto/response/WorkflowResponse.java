package in.elcot.avgcxr.policy.workflow.api.rest.dto.response;

import in.elcot.avgcxr.policy.workflow.domain.model.Workflow;
import java.time.Instant;

public record WorkflowResponse(
    String id, String name, String description, Instant createdAt, Instant updatedAt) {
  public static WorkflowResponse from(Workflow e) {
    return new WorkflowResponse(
        e.getId().toString(), "name", "description", e.getCreatedAt(), e.getUpdatedAt());
  }
}
