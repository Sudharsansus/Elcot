package in.elcot.avgcxr.policy.workflow.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "workflow_history")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WorkflowHistoryEntity {
    @Id
    private UUID id;

    @Column(name = "workflow_instance_id", nullable = false)
    private UUID workflowInstanceId;

    @Column(name = "from_state", length = 100)
    private String fromState;

    @Column(name = "to_state", length = 100, nullable = false)
    private String toState;

    @Column(name = "action", length = 100, nullable = false)
    private String action;

    @Column(name = "actor", length = 200, nullable = false)
    private String actor;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
