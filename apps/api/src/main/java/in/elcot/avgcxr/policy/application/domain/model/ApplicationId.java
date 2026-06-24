package in.elcot.avgcxr.policy.application.domain.model;

import java.io.Serializable;
import java.util.UUID;

public record ApplicationId(UUID value) implements Serializable {
    public static ApplicationId generate() { return new ApplicationId(UUID.randomUUID()); }
    public static ApplicationId of(String v) { return new ApplicationId(UUID.fromString(v)); }
}
