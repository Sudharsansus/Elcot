package in.elcot.avgcxr.platform.file.api.rest.dto.response;

import in.elcot.avgcxr.platform.file.domain.model.File;
import java.time.Instant;

public record FileResponse(
    String id,
    String fileName,
    String originalFileName,
    String mimeType,
    long fileSize,
    String entityType,
    String entityId,
    Instant createdAt) {
  public static FileResponse from(File f) {
    return new FileResponse(
        f.getId().toString(),
        f.getFileName(),
        f.getOriginalFileName(),
        f.getMimeType(),
        f.getFileSize(),
        f.getEntityType(),
        f.getEntityId(),
        f.getCreatedAt());
  }
}
