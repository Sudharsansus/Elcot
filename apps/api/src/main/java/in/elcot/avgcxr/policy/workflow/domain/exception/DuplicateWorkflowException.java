package in.elcot.avgcxr.policy.workflow.domain.exception;



public class DuplicateWorkflowException extends RuntimeException {
    public DuplicateWorkflowException(String field, String value) { super("Workflow already exists with " + field + ": " + value); }
}
