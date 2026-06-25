package in.elcot.avgcxr.platform.notification.api.rest.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponse(UUID id, LocalDateTime createdAt, LocalDateTime updatedAt) {}
