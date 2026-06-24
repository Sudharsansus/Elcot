package in.elcot.avgcxr.platform.audit.domain.model;

import java.io.Serializable;
import java.util.UUID;

public record AuditId(UUID value) implements Serializable {
    public static AuditId generate() { return new AuditId(UUID.randomUUID()); }
    public static AuditId of(String value) { return new AuditId(UUID.fromString(value)); }
    @Override public String toString() { return value.toString(); }
}
