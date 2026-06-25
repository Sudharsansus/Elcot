package in.elcot.avgcxr.policy.workflow.domain.event;

import java.time.Instant;
import java.util.UUID;

public record WorkflowApprovedEvent(UUID workflowId, String taskId, Instant occurredAt) {
  public static WorkflowApprovedEvent from(UUID id, String taskId) {
    return new WorkflowApprovedEvent(id, taskId, Instant.now());
  }
}
