package in.elcot.avgcxr.ecosystem.freelancerregistry.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Freelancerregistry {
    private final FreelancerregistryId id;
    private String name;
    private String description;

    private final Instant createdAt;
    private Instant updatedAt;

    public Freelancerregistry(FreelancerregistryId id, String name) {
        this.id = id;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public FreelancerregistryId getId() { return id; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void markUpdated() { this.updatedAt = Instant.now(); }

}
