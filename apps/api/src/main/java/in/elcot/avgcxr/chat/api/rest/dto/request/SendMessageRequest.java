package in.elcot.avgcxr.chat.api.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SendMessageRequest(
    @NotBlank @Size(max = 4000) String message,
    String sessionToken, // for anonymous users
    String language // "en" or "ta" (optional; auto-detected)
    ) {}
