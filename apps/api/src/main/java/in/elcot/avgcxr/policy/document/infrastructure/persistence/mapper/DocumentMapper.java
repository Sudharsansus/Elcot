package in.elcot.avgcxr.policy.document.infrastructure.persistence.mapper;

import in.elcot.avgcxr.policy.document.domain.model.Document;
import in.elcot.avgcxr.policy.document.infrastructure.persistence.entity.DocumentEntity;

public final class DocumentMapper {
  private DocumentMapper() {}

  public static Document toDomain(DocumentEntity e) {
    if (e == null) return null;
    return Document.builder()
        .id(e.getId())
        .createdAt(e.getCreatedAt())
        .updatedAt(e.getUpdatedAt())
        .build();
  }

  public static DocumentEntity toEntity(Document d) {
    if (d == null) return null;
    DocumentEntity e = new DocumentEntity();
    e.setId(d.getId());
    e.setCreatedAt(d.getCreatedAt());
    e.setUpdatedAt(d.getUpdatedAt());
    return e;
  }
}
