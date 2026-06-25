package in.elcot.avgcxr.policy.document.domain.model;

import java.io.Serializable;
import java.util.UUID;

public record DocumentId(UUID value) implements Serializable {
  public static DocumentId generate() {
    return new DocumentId(UUID.randomUUID());
  }

  public static DocumentId of(String v) {
    return new DocumentId(UUID.fromString(v));
  }
}
