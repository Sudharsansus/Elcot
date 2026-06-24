package in.elcot.avgcxr.ecosystem.businessconnect.api.rest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateBusinessconnectRequest(
    @NotBlank(message = "Name is required") String name,
    String description
) {}
