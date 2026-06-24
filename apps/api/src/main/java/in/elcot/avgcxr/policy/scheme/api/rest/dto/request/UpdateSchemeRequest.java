package in.elcot.avgcxr.policy.scheme.api.rest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateSchemeRequest(@NotBlank String name, String nameTamil, String description, String category, double maxSubsidyAmount) {}
