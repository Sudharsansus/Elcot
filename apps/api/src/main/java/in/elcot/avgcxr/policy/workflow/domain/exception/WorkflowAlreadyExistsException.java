package in.elcot.avgcxr.policy.workflow.domain.exception;

public class WorkflowAlreadyExistsException extends RuntimeException {
  public WorkflowAlreadyExistsException(String businessKey) {
    super("Workflow already exists for: " + businessKey);
  }
}
