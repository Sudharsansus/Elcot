package in.elcot.avgcxr.policy.workflow.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Workflow {
    private final WorkflowId id;
    private String name;
    private String description;

    private final Instant createdAt;
    private Instant updatedAt;

    public Workflow(WorkflowId id, String name) {
        this.id = id;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public WorkflowId getId() { return id; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void markUpdated() { this.updatedAt = Instant.now(); }

}
