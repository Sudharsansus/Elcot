package in.elcot.avgcxr.ecosystem.freelancerregistry.api.rest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateFreelancerregistryRequest(
    @NotBlank(message = "Name is required") String name, String description) {}
