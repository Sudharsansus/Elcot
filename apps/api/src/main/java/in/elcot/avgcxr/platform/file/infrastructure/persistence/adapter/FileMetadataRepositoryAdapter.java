package in.elcot.avgcxr.platform.file.infrastructure.persistence.adapter;

import in.elcot.avgcxr.platform.file.application.port.output.FileMetadataRepositoryPort;
import in.elcot.avgcxr.platform.file.domain.model.FileMetadata;
import in.elcot.avgcxr.platform.file.infrastructure.persistence.mapper.FileMetadataMapper;
import in.elcot.avgcxr.platform.file.infrastructure.persistence.repository.JpaFileMetadataRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FileMetadataRepositoryAdapter implements FileMetadataRepositoryPort {
  private final JpaFileMetadataRepository jpaRepository;

  @Override
  public FileMetadata save(FileMetadata entity) {
    return FileMetadataMapper.toDomain(jpaRepository.save(FileMetadataMapper.toEntity(entity)));
  }

  @Override
  public Optional<FileMetadata> findById(UUID id) {
    return jpaRepository.findById(id).map(FileMetadataMapper::toDomain);
  }

  @Override
  public Page<FileMetadata> findAll(Pageable pageable) {
    return jpaRepository.findAll(pageable).map(FileMetadataMapper::toDomain);
  }

  @Override
  public void deleteById(UUID id) {
    jpaRepository.deleteById(id);
  }
}
