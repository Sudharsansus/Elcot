package in.elcot.avgcxr.platform.audit.api.rest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateAuditRequest(
    @NotBlank(message = "Name is required") String name, String description) {}
