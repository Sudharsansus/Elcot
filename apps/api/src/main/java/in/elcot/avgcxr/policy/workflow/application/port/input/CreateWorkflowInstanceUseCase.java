package in.elcot.avgcxr.policy.workflow.application.port.input;

import in.elcot.avgcxr.policy.workflow.api.rest.dto.response.WorkflowInstanceResponse;
import in.elcot.avgcxr.policy.workflow.application.command.CreateWorkflowInstanceCommand;

public interface CreateWorkflowInstanceUseCase {
    WorkflowInstanceResponse create(CreateWorkflowInstanceCommand command);
}
