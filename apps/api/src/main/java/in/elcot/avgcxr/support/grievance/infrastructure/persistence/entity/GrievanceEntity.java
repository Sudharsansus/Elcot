package in.elcot.avgcxr.support.grievance.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "grievances")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GrievanceEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "grievance_number", length = 50, unique = true)
    private String grievanceNumber;

    @Column(name = "subject", length = 500)
    private String subject;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "application_id")
    private UUID applicationId;

    @Column(name = "raised_by")
    private UUID raisedBy;

    @Column(name = "assigned_to")
    private UUID assignedTo;

    @Column(name = "status", length = 30)
    private String status;

    @Column(name = "priority", length = 20)
    private String priority;

    @Column(name = "resolution", columnDefinition = "TEXT")
    private String resolution;

    @Column(name = "escalation_level")
    private Integer escalationLevel;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
