package in.elcot.avgcxr.support.helpdesk.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "helpdesk_tickets")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class HelpdeskTicketEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "ticket_number", length = 50, unique = true)
    private String ticketNumber;

    @Column(name = "subject", length = 500)
    private String subject;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "priority", length = 20)
    private String priority;

    @Column(name = "status", length = 30)
    private String status;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "assigned_to")
    private UUID assignedTo;

    @Column(name = "resolution", columnDefinition = "TEXT")
    private String resolution;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
