package in.elcot.avgcxr.policy.workflow.domain.exception;

import in.elcot.avgcxr.platformcore.error.ConflictException;

public class WorkflowInstanceAlreadyExistsException extends ConflictException {
    public WorkflowInstanceAlreadyExistsException(String field, String value) {
        super("WORKFLOWINSTANCE_DUPLICATE", "WorkflowInstance already exists with " + field + ": " + value);
    }
}
