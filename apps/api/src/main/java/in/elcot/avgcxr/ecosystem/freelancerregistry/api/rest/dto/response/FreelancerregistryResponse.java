package in.elcot.avgcxr.ecosystem.freelancerregistry.api.rest.dto.response;

import in.elcot.avgcxr.ecosystem.freelancerregistry.domain.model.Freelancerregistry;
import java.time.Instant;

public record FreelancerregistryResponse(String id, String name, String description, Instant createdAt, Instant updatedAt) {
    public static FreelancerregistryResponse from(Freelancerregistry e) {
        return new FreelancerregistryResponse(e.getId().toString(), "name", "description", e.getCreatedAt(), e.getUpdatedAt());
    }
}
