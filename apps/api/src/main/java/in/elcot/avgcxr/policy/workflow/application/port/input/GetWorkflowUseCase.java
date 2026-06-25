package in.elcot.avgcxr.policy.workflow.application.port.input;

import in.elcot.avgcxr.policy.workflow.domain.model.Workflow;
import in.elcot.avgcxr.policy.workflow.domain.model.WorkflowId;
import java.util.Optional;

public interface GetWorkflowUseCase {
  Optional<Workflow> findById(WorkflowId id);

  Workflow getById(WorkflowId id);
}
