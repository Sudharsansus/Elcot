package in.elcot.avgcxr.platform.file.domain.model;

import java.time.Instant;
import java.util.UUID;

public class File {
    private final FileId id;
    private String fileName;
    private String originalFileName;
    private String mimeType;
    private long fileSize;
    private String minioObjectId;
    private String uploadedBy;
    private String entityType;
    private String entityId;
    private final Instant createdAt;
    private Instant updatedAt;

    public File(FileId id, String fileName, String originalFileName, String mimeType, long fileSize, String uploadedBy) {
        this.id = id; this.fileName = fileName; this.originalFileName = originalFileName;
        this.mimeType = mimeType; this.fileSize = fileSize; this.uploadedBy = uploadedBy;
        this.createdAt = Instant.now(); this.updatedAt = Instant.now();
    }

    public FileId getId() { return id; }
    public String getFileName() { return fileName; }
    public String getOriginalFileName() { return originalFileName; }
    public String getMimeType() { return mimeType; }
    public long getFileSize() { return fileSize; }
    public String getMinioObjectId() { return minioObjectId; }
    public String getUploadedBy() { return uploadedBy; }
    public String getEntityType() { return entityType; }
    public String getEntityId() { return entityId; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void attachToEntity(String entityType, String entityId) {
        this.entityType = entityType; this.entityId = entityId; this.updatedAt = Instant.now();
    }
    public void setMinioObjectId(String oid) { this.minioObjectId = oid; this.updatedAt = Instant.now(); }
    public void markUpdated() { this.updatedAt = Instant.now(); }
}
