package in.elcot.avgcxr.platform.audit.domain.model;

import java.time.Instant;

public class Audit {
  private final AuditId id;
  private String name;
  private String description;

  private final Instant createdAt;
  private Instant updatedAt;

  public Audit(AuditId id, String name) {
    this.id = id;
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  public AuditId getId() {
    return id;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void markUpdated() {
    this.updatedAt = Instant.now();
  }
}
