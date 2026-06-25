package in.elcot.avgcxr.policy.application.domain.model;

import in.elcot.avgcxr.platformcore.model.AuditableEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Pure domain model — no framework imports. Persistence is handled by
 * infrastructure.persistence.entity + mapper.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Application extends AuditableEntity {

  private UUID id;

  private String applicationNumber;

  private UUID schemeId;
  private UUID applicantId;
  private String district;

  @Builder.Default private ApplicationStatus status = ApplicationStatus.DRAFT;

  private LocalDateTime submittedAt;
  private LocalDateTime reviewedAt;
  private LocalDateTime approvedAt;
  private LocalDateTime rejectedAt;
  private String rejectionReason;
  private BigDecimal fundingApproved;

  public enum ApplicationStatus {
    DRAFT,
    SUBMITTED,
    UNDER_REVIEW,
    APPROVED,
    REJECTED,
    WITHDRAWN,
    FUND_DISBURSED;

    public boolean canTransitionTo(ApplicationStatus target) {
      return switch (this) {
        case DRAFT -> target == SUBMITTED || target == WITHDRAWN;
        case SUBMITTED -> target == UNDER_REVIEW || target == WITHDRAWN;
        case UNDER_REVIEW -> target == APPROVED || target == REJECTED;
        case APPROVED -> target == FUND_DISBURSED;
        default -> false;
      };
    }
  }

  public static Application createDraft(
      UUID schemeId, UUID applicantId, String district, String appNumber) {
    return Application.builder()
        .id(UUID.randomUUID())
        .applicationNumber(appNumber)
        .schemeId(schemeId)
        .applicantId(applicantId)
        .district(district)
        .status(ApplicationStatus.DRAFT)
        .build();
  }

  public void submit() {
    transitionTo(ApplicationStatus.SUBMITTED);
    this.submittedAt = LocalDateTime.now();
  }

  public void approve(BigDecimal funding) {
    transitionTo(ApplicationStatus.APPROVED);
    this.fundingApproved = funding;
    this.approvedAt = LocalDateTime.now();
  }

  public void reject(String reason) {
    transitionTo(ApplicationStatus.REJECTED);
    this.rejectionReason = reason;
    this.rejectedAt = LocalDateTime.now();
  }

  public void withdraw() {
    transitionTo(ApplicationStatus.WITHDRAWN);
  }

  private void transitionTo(ApplicationStatus target) {
    if (!this.status.canTransitionTo(target)) {
      throw new IllegalStateException("Cannot transition from " + this.status + " to " + target);
    }
    this.status = target;
  }
}
