package in.elcot.avgcxr.platform.file.domain.event;

import java.time.Instant;
import java.util.UUID;

public record FileCreatedEvent(
    UUID fileId, String fileName, String mimeType, long fileSize, Instant occurredAt) {
  public static FileCreatedEvent from(UUID id, String fn, String mime, long size) {
    return new FileCreatedEvent(id, fn, mime, size, Instant.now());
  }
}
