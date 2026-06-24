package in.elcot.avgcxr.platform.notification.api.rest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateNotificationRequest(@NotBlank String userId, @NotBlank String subject, @NotBlank String body, String channel) {}
