package in.elcot.avgcxr.analytics.reporting.api.rest.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReportDataResponse(
    UUID id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
