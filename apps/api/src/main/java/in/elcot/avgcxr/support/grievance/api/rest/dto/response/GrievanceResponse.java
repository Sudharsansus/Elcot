package in.elcot.avgcxr.support.grievance.api.rest.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record GrievanceResponse(
    UUID id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
