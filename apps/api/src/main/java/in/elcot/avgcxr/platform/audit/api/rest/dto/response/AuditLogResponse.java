package in.elcot.avgcxr.platform.audit.api.rest.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuditLogResponse(
    UUID id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
