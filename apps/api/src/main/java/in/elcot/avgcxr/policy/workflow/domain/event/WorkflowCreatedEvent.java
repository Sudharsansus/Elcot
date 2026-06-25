package in.elcot.avgcxr.policy.workflow.domain.event;

import java.time.Instant;
import java.util.UUID;

public record WorkflowCreatedEvent(UUID workflowId, String action, Instant occurredAt) {
  public static WorkflowCreatedEvent from(UUID id) {
    return new WorkflowCreatedEvent(id, "CREATED", Instant.now());
  }
}
