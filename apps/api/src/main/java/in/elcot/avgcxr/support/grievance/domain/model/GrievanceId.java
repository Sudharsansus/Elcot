package in.elcot.avgcxr.support.grievance.domain.model;

import java.io.Serializable;
import java.util.UUID;

public record GrievanceId(UUID value) implements Serializable {
  public static GrievanceId generate() {
    return new GrievanceId(UUID.randomUUID());
  }

  public static GrievanceId of(String v) {
    return new GrievanceId(UUID.fromString(v));
  }
}
