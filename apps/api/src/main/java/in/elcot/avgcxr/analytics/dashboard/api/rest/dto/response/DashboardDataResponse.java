package in.elcot.avgcxr.analytics.dashboard.api.rest.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record DashboardDataResponse(UUID id, LocalDateTime createdAt, LocalDateTime updatedAt) {}
