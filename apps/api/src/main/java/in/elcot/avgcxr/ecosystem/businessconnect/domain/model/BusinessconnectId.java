package in.elcot.avgcxr.ecosystem.businessconnect.domain.model;

import java.io.Serializable;
import java.util.UUID;

public record BusinessconnectId(UUID value) implements Serializable {
    public static BusinessconnectId generate() { return new BusinessconnectId(UUID.randomUUID()); }
    public static BusinessconnectId of(String value) { return new BusinessconnectId(UUID.fromString(value)); }
    @Override public String toString() { return value.toString(); }
}
