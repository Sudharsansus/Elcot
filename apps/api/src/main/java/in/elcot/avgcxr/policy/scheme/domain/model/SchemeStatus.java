package in.elcot.avgcxr.policy.scheme.domain.model;

public enum SchemeStatus {
  DRAFT,
  PUBLISHED,
  OPEN,
  CLOSED,
  ARCHIVED;

  public boolean isEditable() {
    return this == DRAFT || this == PUBLISHED;
  }

  public boolean isApplicable() {
    return this == OPEN;
  }
}
