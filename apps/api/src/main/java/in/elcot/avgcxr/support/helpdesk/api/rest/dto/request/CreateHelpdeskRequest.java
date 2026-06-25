package in.elcot.avgcxr.support.helpdesk.api.rest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateHelpdeskRequest(
    @NotBlank(message = "Name is required") String name, String description) {}
