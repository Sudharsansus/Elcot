package in.elcot.avgcxr.platform.file.api.rest.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record FileMetadataResponse(
    UUID id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
