package in.elcot.avgcxr.policy.application.domain.model;

public enum ApplicationStatus {
  DRAFT,
  SUBMITTED,
  UNDER_REVIEW,
  ADDITIONAL_INFO_REQUESTED,
  APPROVED,
  REJECTED,
  DISBURSED,
  WITHDRAWN;

  public boolean isTerminal() {
    return this == APPROVED || this == REJECTED || this == WITHDRAWN;
  }

  public boolean isEditable() {
    return this == DRAFT || this == ADDITIONAL_INFO_REQUESTED;
  }

  public boolean canWithdraw() {
    return this == SUBMITTED || this == UNDER_REVIEW || this == ADDITIONAL_INFO_REQUESTED;
  }
}
