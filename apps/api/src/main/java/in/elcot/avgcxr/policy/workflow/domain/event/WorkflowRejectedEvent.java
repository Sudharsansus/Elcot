package in.elcot.avgcxr.policy.workflow.domain.event;

import java.time.Instant;
import java.util.UUID;

public record WorkflowRejectedEvent(
    UUID workflowId, String taskId, String reason, Instant occurredAt) {
  public static WorkflowRejectedEvent from(UUID id, String taskId, String reason) {
    return new WorkflowRejectedEvent(id, taskId, reason, Instant.now());
  }
}
