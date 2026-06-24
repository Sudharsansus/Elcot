package in.elcot.avgcxr.ecosystem.freelancerregistry.api.rest.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record FreelancerProfileResponse(
    UUID id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
