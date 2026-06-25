package in.elcot.avgcxr.policy.workflow.application.port.output;

import in.elcot.avgcxr.policy.workflow.domain.model.Workflow;
import in.elcot.avgcxr.policy.workflow.domain.model.WorkflowId;
import java.util.Optional;

/** Output port for Workflow persistence - implemented by infrastructure adapter */
public interface WorkflowRepositoryPort {
  Workflow save(Workflow entity);

  Optional<Workflow> findById(WorkflowId id);

  void deleteById(WorkflowId id);

  boolean existsById(WorkflowId id);
}
