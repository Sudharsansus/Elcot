package in.elcot.avgcxr.platform.file.api.rest.dto.request;

public record CreateFileRequest(
    String originalFileName, String mimeType, long fileSize, String entityType, String entityId) {}
