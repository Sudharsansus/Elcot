package in.elcot.avgcxr.platform.file.application.port.output;

import in.elcot.avgcxr.platform.file.domain.model.FileMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface FileMetadataRepositoryPort {
    FileMetadata save(FileMetadata entity);
    Optional<FileMetadata> findById(UUID id);
    Page<FileMetadata> findAll(Pageable pageable);
    void deleteById(UUID id);
}
