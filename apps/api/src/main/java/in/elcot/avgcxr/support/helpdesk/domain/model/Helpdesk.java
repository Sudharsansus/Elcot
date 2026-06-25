package in.elcot.avgcxr.support.helpdesk.domain.model;

import java.time.Instant;

public class Helpdesk {
  private final HelpdeskId id;
  private String name;
  private String description;

  private final Instant createdAt;
  private Instant updatedAt;

  public Helpdesk(HelpdeskId id, String name) {
    this.id = id;
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  public HelpdeskId getId() {
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
