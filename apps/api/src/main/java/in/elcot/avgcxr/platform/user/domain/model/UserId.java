package in.elcot.avgcxr.platform.user.domain.model;

import java.io.Serializable;
import java.util.UUID;

public record UserId(UUID value) implements Serializable {
    public static UserId generate() { return new UserId(UUID.randomUUID()); }
    public static UserId of(String v) { return new UserId(UUID.fromString(v)); }
    @Override public String toString() { return value.toString(); }
}
