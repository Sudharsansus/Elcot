package in.elcot.avgcxr.policy.workflow.domain.exception;

public class InvalidWorkflowStateException extends RuntimeException {
  public InvalidWorkflowStateException(String current, String target) {
    super("Invalid workflow state transition: " + current + " -> " + target);
  }
}
