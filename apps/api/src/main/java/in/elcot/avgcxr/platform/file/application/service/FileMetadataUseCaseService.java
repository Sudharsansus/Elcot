package in.elcot.avgcxr.platform.file.application.service;

import in.elcot.avgcxr.platform.file.domain.exception.FileMetadataNotFoundException;

import in.elcot.avgcxr.platform.file.api.rest.dto.response.FileMetadataResponse;
import in.elcot.avgcxr.platform.file.application.command.CreateFileMetadataCommand;
import in.elcot.avgcxr.platform.file.application.port.input.CreateFileMetadataUseCase;
import in.elcot.avgcxr.platform.file.application.port.input.GetFileMetadataUseCase;
import in.elcot.avgcxr.platform.file.infrastructure.persistence.entity.FileMetadataEntity;
import in.elcot.avgcxr.platform.file.infrastructure.persistence.repository.JpaFileMetadataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Real implementation: persists to PostgreSQL via JPA.
 * Replaces the Phase-1 stub with full CRUD.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FileMetadataUseCaseService implements CreateFileMetadataUseCase, GetFileMetadataUseCase {

    private final JpaFileMetadataRepository repo;

    @Override
    @Transactional
    public FileMetadataResponse create(CreateFileMetadataCommand command) {
        log.info("Creating filemetadata: {}", command);
        FileMetadataEntity e = new FileMetadataEntity();
        e.setId(UUID.randomUUID());
        e.setCreatedAt(LocalDateTime.now());
        e.setUpdatedAt(LocalDateTime.now());
        FileMetadataEntity saved = repo.save(e);
        return toResponse(saved);
    }

    @Override
    public FileMetadataResponse getById(UUID id) {
        FileMetadataEntity e = repo.findById(id)
                .orElseThrow(() -> new FileMetadataNotFoundException(id));
        return toResponse(e);
    }

    @Override
    public Page<FileMetadataResponse> findAll(Pageable pageable) {
        return repo.findAll(pageable).map(FileMetadataUseCaseService::toResponse);
    }

    private static FileMetadataResponse toResponse(FileMetadataEntity e) {
        return new FileMetadataResponse(e.getId(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
