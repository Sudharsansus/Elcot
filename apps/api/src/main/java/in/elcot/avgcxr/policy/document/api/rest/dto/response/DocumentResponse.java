package in.elcot.avgcxr.policy.document.api.rest.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record DocumentResponse(UUID id, LocalDateTime createdAt, LocalDateTime updatedAt) {}
