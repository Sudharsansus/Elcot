package in.elcot.avgcxr.policy.workflow.api.rest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateWorkflowRequest(
    @NotBlank(message = "Name is required") String name, String description) {}
