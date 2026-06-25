package in.elcot.avgcxr.policy.workflow.application.port.input;

import in.elcot.avgcxr.policy.workflow.application.command.ProcessWorkflowCommand;

public interface ProcessWorkflowUseCase {
  void process(ProcessWorkflowCommand command);

  void claimTask(String taskId, String userId);

  void unclaimTask(String taskId);
}
