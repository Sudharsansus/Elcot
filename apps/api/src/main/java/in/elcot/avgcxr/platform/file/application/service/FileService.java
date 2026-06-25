package in.elcot.avgcxr.platform.file.application.service;

import in.elcot.avgcxr.platform.file.application.command.CreateFileCommand;
import in.elcot.avgcxr.platform.file.application.port.input.CreateFileUseCase;
import in.elcot.avgcxr.platform.file.application.port.input.GetFileUseCase;
import in.elcot.avgcxr.platform.file.application.port.output.FileRepositoryPort;
import in.elcot.avgcxr.platform.file.application.port.output.FileStoragePort;
import in.elcot.avgcxr.platform.file.domain.exception.FileNotFoundException;
import in.elcot.avgcxr.platform.file.domain.model.File;
import in.elcot.avgcxr.platform.file.domain.model.FileId;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * File service: persists metadata via FileRepositoryPort AND uploads the byte payload via
 * FileStoragePort (MinIO). Both outputs are required for the tender document-upload requirement.
 */
@Service
@Transactional(readOnly = true)
public class FileService implements CreateFileUseCase, GetFileUseCase {

  private static final Logger log = LoggerFactory.getLogger(FileService.class);

  private final FileRepositoryPort repository;
  private final FileStoragePort storage;

  public FileService(FileRepositoryPort repository, FileStoragePort storage) {
    this.repository = repository;
    this.storage = storage;
  }

  @Override
  @Transactional
  public File create(CreateFileCommand cmd) {
    if (cmd.content() == null || cmd.content().length == 0) {
      throw new IllegalArgumentException("File content is required (cannot be empty)");
    }
    String objectName = "uploads/" + UUID.randomUUID();
    log.info(
        "Uploading file {} ({} bytes) to object storage", cmd.originalFileName(), cmd.fileSize());
    String storageKey = storage.upload(objectName, cmd.content(), cmd.mimeType());

    File file =
        new File(
            FileId.generate(),
            storageKey,
            cmd.originalFileName(),
            cmd.mimeType(),
            cmd.fileSize(),
            cmd.uploadedBy());
    file.attachToEntity(cmd.entityType(), cmd.entityId());
    File saved = repository.save(file);
    log.info("Persisted file metadata id={} storageKey={}", saved.getId(), storageKey);
    return saved;
  }

  @Override
  public Optional<File> findById(FileId id) {
    return repository.findById(id);
  }

  @Override
  public File getById(FileId id) {
    return repository.findById(id).orElseThrow(() -> new FileNotFoundException(id.value()));
  }
}
