package in.elcot.avgcxr.policy.application.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "applications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ApplicationEntity {
    @Id @Column(name = "id") private UUID id;
    @Column(name = "application_number", nullable = false, unique = true) private String applicationNumber;
    @Column(name = "scheme_id", nullable = false) private UUID schemeId;
    @Column(name = "applicant_id", nullable = false) private UUID applicantId;
    @Column(name = "district", length = 100) private String district;
    @Column(name = "status", nullable = false) private String status;
    @Column(name = "submitted_at") private LocalDateTime submittedAt;
    @Column(name = "reviewed_at") private LocalDateTime reviewedAt;
    @Column(name = "approved_at") private LocalDateTime approvedAt;
    @Column(name = "rejected_at") private LocalDateTime rejectedAt;
    @Column(name = "rejection_reason", columnDefinition = "TEXT") private String rejectionReason;
    @Column(name = "funding_approved", precision = 15, scale = 2) private BigDecimal fundingApproved;
    @Column(name = "created_at", nullable = false) private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
}
