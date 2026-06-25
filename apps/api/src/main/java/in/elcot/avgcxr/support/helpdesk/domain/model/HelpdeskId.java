package in.elcot.avgcxr.support.helpdesk.domain.model;

import java.io.Serializable;
import java.util.UUID;

public record HelpdeskId(UUID value) implements Serializable {
  public static HelpdeskId generate() {
    return new HelpdeskId(UUID.randomUUID());
  }

  public static HelpdeskId of(String value) {
    return new HelpdeskId(UUID.fromString(value));
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
