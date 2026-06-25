package in.elcot.avgcxr.platform.file.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "file_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileMetadataEntity {
  @Id
  @Column(name = "id")
  private UUID id;

  @Column(name = "name", length = 255)
  private String name;

  @Column(name = "mime_type", length = 100)
  private String mimeType;

  @Column(name = "size_bytes")
  private Long sizeBytes;

  @Column(name = "checksum", length = 100)
  private String checksum;

  @Column(name = "uploaded_by", length = 40)
  private String uploadedBy;

  @Column(name = "entity_type", length = 50)
  private String entityType;

  @Column(name = "entity_id", length = 40)
  private String entityId;

  @Column(name = "storage_key", length = 500)
  private String storageKey;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
}
