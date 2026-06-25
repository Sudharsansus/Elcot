package in.elcot.avgcxr.policy.workflow.api.rest.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record WorkflowInstanceResponse(UUID id, LocalDateTime createdAt, LocalDateTime updatedAt) {}
