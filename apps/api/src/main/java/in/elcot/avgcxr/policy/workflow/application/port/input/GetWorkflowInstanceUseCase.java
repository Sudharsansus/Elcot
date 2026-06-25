package in.elcot.avgcxr.policy.workflow.application.port.input;

import in.elcot.avgcxr.policy.workflow.api.rest.dto.response.WorkflowInstanceResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetWorkflowInstanceUseCase {
  WorkflowInstanceResponse getById(UUID id);

  Page<WorkflowInstanceResponse> findAll(Pageable pageable);
}
