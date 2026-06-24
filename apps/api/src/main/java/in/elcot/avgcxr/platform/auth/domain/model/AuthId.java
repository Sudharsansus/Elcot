package in.elcot.avgcxr.platform.auth.domain.model;

import java.io.Serializable;
import java.util.UUID;

public record AuthId(UUID value) implements Serializable {
    public static AuthId generate() { return new AuthId(UUID.randomUUID()); }
    public static AuthId of(String v) { return new AuthId(UUID.fromString(v)); }
}
