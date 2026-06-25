package in.elcot.avgcxr.policy.document.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "application_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentEntity {
  @Id
  @Column(name = "id")
  private UUID id;

  @Column(name = "name", length = 200)
  private String name;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "document_type", length = 50)
  private String documentType;

  @Column(name = "file_url", length = 500)
  private String fileUrl;

  @Column(name = "application_id")
  private UUID applicationId;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
}
