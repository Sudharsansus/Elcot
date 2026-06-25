package in.elcot.avgcxr.platform.search.api.rest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateSearchRequest(
    @NotBlank(message = "Name is required") String name, String description) {}
