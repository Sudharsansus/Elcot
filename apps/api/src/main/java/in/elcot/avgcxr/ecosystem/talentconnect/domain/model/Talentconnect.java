package in.elcot.avgcxr.ecosystem.talentconnect.domain.model;

import java.time.Instant;

public class Talentconnect {
  private final TalentconnectId id;
  private String name;
  private String description;

  private final Instant createdAt;
  private Instant updatedAt;

  public Talentconnect(TalentconnectId id, String name) {
    this.id = id;
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  public TalentconnectId getId() {
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
