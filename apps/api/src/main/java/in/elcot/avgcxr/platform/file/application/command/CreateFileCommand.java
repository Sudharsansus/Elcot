package in.elcot.avgcxr.platform.file.application.command;

/**
 * Command for creating a file record. The byte payload travels in
 * {@code content} so the application service can hand it to the
 * storage port (MinIO) without going back through the web layer.
 */
public record CreateFileCommand(
    String originalFileName,
    String mimeType,
    long fileSize,
    String uploadedBy,
    String entityType,
    String entityId,
    byte[] content
) {}
