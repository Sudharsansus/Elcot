package in.elcot.avgcxr.platform.file.infrastructure.persistence.mapper;

import in.elcot.avgcxr.platform.file.domain.model.FileMetadata;
import in.elcot.avgcxr.platform.file.infrastructure.persistence.entity.FileMetadataEntity;

public final class FileMetadataMapper {
  private FileMetadataMapper() {}

  public static FileMetadata toDomain(FileMetadataEntity e) {
    if (e == null) return null;
    return FileMetadata.builder()
        .id(e.getId())
        .createdAt(e.getCreatedAt())
        .updatedAt(e.getUpdatedAt())
        .build();
  }

  public static FileMetadataEntity toEntity(FileMetadata d) {
    if (d == null) return null;
    FileMetadataEntity e = new FileMetadataEntity();
    e.setId(d.getId());
    e.setCreatedAt(d.getCreatedAt());
    e.setUpdatedAt(d.getUpdatedAt());
    return e;
  }
}
