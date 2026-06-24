package in.elcot.avgcxr.platform.file.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "files")
public class FileEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(name = "id", columnDefinition = "UUID") private String id;
    @Column(name = "file_name") private String fileName;
    @Column(name = "original_file_name") private String originalFileName;
    @Column(name = "mime_type", length = 100) private String mimeType;
    @Column(name = "file_size") private long fileSize;
    @Column(name = "minio_object_id") private String minioObjectId;
    @Column(name = "uploaded_by") private String uploadedBy;
    @Column(name = "entity_type", length = 50) private String entityType;
    @Column(name = "entity_id") private String entityId;
    @Column(name = "created_at", nullable = false) private Instant createdAt;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt;
    public String getId() { return id; } public void setId(String v) { id = v; }
    public String getFileName() { return fileName; } public void setFileName(String v) { fileName = v; }
    public String getOriginalFileName() { return originalFileName; } public void setOriginalFileName(String v) { originalFileName = v; }
    public String getMimeType() { return mimeType; } public void setMimeType(String v) { mimeType = v; }
    public long getFileSize() { return fileSize; } public void setFileSize(long v) { fileSize = v; }
    public String getMinioObjectId() { return minioObjectId; } public void setMinioObjectId(String v) { minioObjectId = v; }
    public String getUploadedBy() { return uploadedBy; } public void setUploadedBy(String v) { uploadedBy = v; }
    public String getEntityType() { return entityType; } public void setEntityType(String v) { entityType = v; }
    public String getEntityId() { return entityId; } public void setEntityId(String v) { entityId = v; }
    public Instant getCreatedAt() { return createdAt; } public void setCreatedAt(Instant v) { createdAt = v; }
    public Instant getUpdatedAt() { return updatedAt; } public void setUpdatedAt(Instant v) { updatedAt = v; }
}

