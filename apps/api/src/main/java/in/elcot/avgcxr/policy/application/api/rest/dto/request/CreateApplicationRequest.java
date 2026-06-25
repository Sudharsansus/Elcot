package in.elcot.avgcxr.policy.application.api.rest.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record CreateApplicationRequest(
    @NotNull(message = "Scheme ID is required") UUID schemeId,
    UUID applicantId,
    @Size(max = 100) String district,
    String formData) {}
