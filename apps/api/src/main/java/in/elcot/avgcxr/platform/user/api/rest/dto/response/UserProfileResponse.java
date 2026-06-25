package in.elcot.avgcxr.platform.user.api.rest.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserProfileResponse(UUID id, LocalDateTime createdAt, LocalDateTime updatedAt) {}
