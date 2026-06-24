package in.elcot.avgcxr.policy.workflow.domain.event;

import java.time.Instant;
import java.util.UUID;

public record WorkflowUpdatedEvent(UUID workflowId, String field, String oldValue, String newValue, Instant occurredAt) {
    public static WorkflowUpdatedEvent from(UUID id, String field, String oldVal, String newVal) {
        return new WorkflowUpdatedEvent(id, field, oldVal, newVal, Instant.now());
    }
}
