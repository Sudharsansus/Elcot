package in.elcot.avgcxr.support.grievance.api.rest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateGrievanceRequest(
    @NotBlank String subject,
    @NotBlank String description,
    String applicationId,
    String category) {}
