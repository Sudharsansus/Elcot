package in.elcot.avgcxr.platform.auth.api.rest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateAuthRequest(
    @NotBlank String username, @NotBlank String password, String captchaToken) {}
