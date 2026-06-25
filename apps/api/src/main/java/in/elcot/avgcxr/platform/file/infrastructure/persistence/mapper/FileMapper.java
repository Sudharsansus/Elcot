package in.elcot.avgcxr.platform.file.infrastructure.persistence.mapper;

import in.elcot.avgcxr.platform.file.domain.model.File;
import in.elcot.avgcxr.platform.file.domain.model.FileId;
import in.elcot.avgcxr.platform.file.infrastructure.persistence.entity.FileEntity;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {
  public FileEntity toEntity(File d) {
    FileEntity e = new FileEntity();
    e.setId(d.getId().toString());
    e.setFileName(d.getFileName());
    e.setOriginalFileName(d.getOriginalFileName());
    e.setMimeType(d.getMimeType());
    e.setFileSize(d.getFileSize());
    e.setMinioObjectId(d.getMinioObjectId());
    e.setUploadedBy(d.getUploadedBy());
    e.setEntityType(d.getEntityType());
    e.setEntityId(d.getEntityId());
    e.setCreatedAt(d.getCreatedAt());
    e.setUpdatedAt(d.getUpdatedAt());
    return e;
  }

  public File toDomain(FileEntity e) {
    File f =
        new File(
            FileId.of(e.getId()),
            e.getFileName(),
            e.getOriginalFileName(),
            e.getMimeType(),
            e.getFileSize(),
            e.getUploadedBy());
    f.attachToEntity(e.getEntityType(), e.getEntityId());
    return f;
  }
}
