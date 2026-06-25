package in.elcot.avgcxr.policy.workflow.application.port.output;

import in.elcot.avgcxr.policy.workflow.domain.model.WorkflowInstance;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkflowInstanceRepositoryPort {
  WorkflowInstance save(WorkflowInstance entity);

  Optional<WorkflowInstance> findById(UUID id);

  Page<WorkflowInstance> findAll(Pageable pageable);

  void deleteById(UUID id);
}
