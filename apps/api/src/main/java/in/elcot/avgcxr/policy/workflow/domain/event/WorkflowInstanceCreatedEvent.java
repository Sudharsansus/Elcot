package in.elcot.avgcxr.policy.workflow.domain.event;

import in.elcot.avgcxr.platformevents.domain.DomainEvent;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class WorkflowInstanceCreatedEvent extends DomainEvent {
  public WorkflowInstanceCreatedEvent(UUID entityId) {
    super(
        UUID.randomUUID(),
        "WORKFLOWINSTANCE_CREATED",
        LocalDateTime.now(),
        entityId.toString(),
        Map.of("entityId", entityId.toString()));
  }
}
