package in.elcot.avgcxr.platform.search.domain.model;

import java.io.Serializable;
import java.util.UUID;

public record SearchId(UUID value) implements Serializable {
    public static SearchId generate() { return new SearchId(UUID.randomUUID()); }
    public static SearchId of(String value) { return new SearchId(UUID.fromString(value)); }
    @Override public String toString() { return value.toString(); }
}
