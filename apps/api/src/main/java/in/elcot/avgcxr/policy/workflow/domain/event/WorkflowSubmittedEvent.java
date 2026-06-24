package in.elcot.avgcxr.policy.workflow.domain.event;

import java.time.Instant;
import java.util.UUID;

public record WorkflowSubmittedEvent(UUID workflowId, String processKey, Instant occurredAt) {
    public static WorkflowSubmittedEvent from(UUID id, String key) { return new WorkflowSubmittedEvent(id, key, Instant.now()); }
}
