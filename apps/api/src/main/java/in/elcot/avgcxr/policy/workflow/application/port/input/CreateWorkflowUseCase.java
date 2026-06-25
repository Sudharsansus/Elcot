package in.elcot.avgcxr.policy.workflow.application.port.input;

import in.elcot.avgcxr.policy.workflow.application.command.CreateWorkflowCommand;
import in.elcot.avgcxr.policy.workflow.domain.model.Workflow;

public interface CreateWorkflowUseCase {
  Workflow create(CreateWorkflowCommand command);
}
