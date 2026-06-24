package in.elcot.avgcxr.policy.workflow.application.port.input;

import in.elcot.avgcxr.policy.workflow.api.rest.dto.response.WorkflowInstanceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface GetWorkflowInstanceUseCase {
    WorkflowInstanceResponse getById(UUID id);
    Page<WorkflowInstanceResponse> findAll(Pageable pageable);
}
