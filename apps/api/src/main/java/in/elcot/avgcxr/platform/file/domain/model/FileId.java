package in.elcot.avgcxr.platform.file.domain.model;

import java.io.Serializable;
import java.util.UUID;

public record FileId(UUID value) implements Serializable {
    public static FileId generate() { return new FileId(UUID.randomUUID()); }
    public static FileId of(String v) { return new FileId(UUID.fromString(v)); }
}
