package in.elcot.avgcxr.platform.file.infrastructure.persistence.repository;

import in.elcot.avgcxr.platform.file.infrastructure.persistence.entity.FileMetadataEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaFileMetadataRepository extends JpaRepository<FileMetadataEntity, UUID> {}
