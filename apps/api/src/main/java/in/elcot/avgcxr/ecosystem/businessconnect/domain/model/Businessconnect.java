package in.elcot.avgcxr.ecosystem.businessconnect.domain.model;

import java.time.Instant;

public class Businessconnect {
  private final BusinessconnectId id;
  private String name;
  private String description;

  private final Instant createdAt;
  private Instant updatedAt;

  public Businessconnect(BusinessconnectId id, String name) {
    this.id = id;
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  public BusinessconnectId getId() {
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
