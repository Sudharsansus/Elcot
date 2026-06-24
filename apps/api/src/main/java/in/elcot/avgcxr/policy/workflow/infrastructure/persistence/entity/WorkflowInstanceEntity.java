package in.elcot.avgcxr.policy.workflow.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "workflow_instances")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class WorkflowInstanceEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "process_definition_id", length = 255)
    private String processDefinitionId;

    @Column(name = "process_definition_key", length = 100)
    private String processDefinitionKey;

    @Column(name = "business_key", length = 255)
    private String businessKey;

    @Column(name = "status", length = 30)
    private String status;

    @Column(name = "started_by")
    private UUID startedBy;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "variables", columnDefinition = "JSONB")
    private String variables;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
