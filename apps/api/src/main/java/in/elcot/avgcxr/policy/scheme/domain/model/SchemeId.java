package in.elcot.avgcxr.policy.scheme.domain.model;

import java.io.Serializable;
import java.util.UUID;

public record SchemeId(UUID value) implements Serializable {
  public static SchemeId generate() {
    return new SchemeId(UUID.randomUUID());
  }

  public static SchemeId of(String v) {
    return new SchemeId(UUID.fromString(v));
  }
}
