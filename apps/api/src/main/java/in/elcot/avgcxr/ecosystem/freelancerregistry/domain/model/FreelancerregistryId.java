package in.elcot.avgcxr.ecosystem.freelancerregistry.domain.model;

import java.io.Serializable;
import java.util.UUID;

public record FreelancerregistryId(UUID value) implements Serializable {
  public static FreelancerregistryId generate() {
    return new FreelancerregistryId(UUID.randomUUID());
  }

  public static FreelancerregistryId of(String value) {
    return new FreelancerregistryId(UUID.fromString(value));
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
