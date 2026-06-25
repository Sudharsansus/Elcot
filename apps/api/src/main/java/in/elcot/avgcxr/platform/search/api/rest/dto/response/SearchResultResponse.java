package in.elcot.avgcxr.platform.search.api.rest.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record SearchResultResponse(UUID id, LocalDateTime createdAt, LocalDateTime updatedAt) {}
