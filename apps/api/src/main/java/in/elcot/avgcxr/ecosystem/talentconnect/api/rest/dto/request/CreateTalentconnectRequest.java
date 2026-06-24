package in.elcot.avgcxr.ecosystem.talentconnect.api.rest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateTalentconnectRequest(
    @NotBlank(message = "Name is required") String name,
    String description
) {}
