package in.elcot.avgcxr.ecosystem.talentconnect.domain.model;

import java.io.Serializable;
import java.util.UUID;

public record TalentconnectId(UUID value) implements Serializable {
  public static TalentconnectId generate() {
    return new TalentconnectId(UUID.randomUUID());
  }

  public static TalentconnectId of(String value) {
    return new TalentconnectId(UUID.fromString(value));
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
