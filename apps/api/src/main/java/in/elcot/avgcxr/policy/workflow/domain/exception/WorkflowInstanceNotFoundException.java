package in.elcot.avgcxr.policy.workflow.domain.exception;

import in.elcot.avgcxr.platformcore.error.NotFoundException;
import java.util.UUID;

public class WorkflowInstanceNotFoundException extends NotFoundException {
  public WorkflowInstanceNotFoundException(UUID id) {
    super("WORKFLOWINSTANCE_NOT_FOUND", "WorkflowInstance not found with id: " + id);
  }
}
