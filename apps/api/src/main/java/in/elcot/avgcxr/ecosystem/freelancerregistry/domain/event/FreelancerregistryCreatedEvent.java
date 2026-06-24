package in.elcot.avgcxr.ecosystem.freelancerregistry.domain.event;

import java.time.Instant;
import java.util.UUID;

public record FreelancerregistryCreatedEvent(UUID freelancerregistryId, String action, Instant occurredAt) {
    public static FreelancerregistryCreatedEvent from(UUID id) { return new FreelancerregistryCreatedEvent(id, "CREATED", Instant.now()); }
}
