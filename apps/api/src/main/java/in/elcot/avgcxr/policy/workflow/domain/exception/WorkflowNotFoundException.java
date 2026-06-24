package in.elcot.avgcxr.policy.workflow.domain.exception;

import java.util.UUID;

public class WorkflowNotFoundException extends RuntimeException {
    public WorkflowNotFoundException(UUID id) { super("Workflow not found with id: " + id); }
    public WorkflowNotFoundException(String identifier) { super("Workflow not found: " + identifier); }
}
