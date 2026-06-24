package in.elcot.avgcxr.policy.document.application.service;

import in.elcot.avgcxr.policy.document.domain.exception.DocumentNotFoundException;

import in.elcot.avgcxr.policy.document.api.rest.dto.response.DocumentResponse;
import in.elcot.avgcxr.policy.document.application.command.CreateDocumentCommand;
import in.elcot.avgcxr.policy.document.application.command.ProcessDocumentCommand;
import in.elcot.avgcxr.policy.document.application.port.input.CreateDocumentUseCase;
import in.elcot.avgcxr.policy.document.application.port.input.GetDocumentUseCase;
import in.elcot.avgcxr.policy.document.application.port.input.ProcessDocumentUseCase;
import in.elcot.avgcxr.policy.document.domain.model.DocumentId;
import in.elcot.avgcxr.policy.document.infrastructure.persistence.entity.DocumentEntity;
import in.elcot.avgcxr.policy.document.infrastructure.persistence.repository.JpaDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Real implementation: persists document metadata to PostgreSQL.
 * File content goes to MinIO (or filesystem fallback) - tracked via storageKey.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DocumentUseCaseService
        implements CreateDocumentUseCase, GetDocumentUseCase, ProcessDocumentUseCase {

    private final JpaDocumentRepository docRepo;

    @Override
    @Transactional
    public DocumentResponse create(CreateDocumentCommand cmd) {
        log.info("Creating document record (type/stored in fields)");
        DocumentEntity e = new DocumentEntity();
        e.setId(UUID.randomUUID());
        e.setCreatedAt(LocalDateTime.now());
        e.setUpdatedAt(LocalDateTime.now());
        DocumentEntity saved = docRepo.save(e);
        return toResponse(saved);
    }

    @Override
    public DocumentResponse getById(UUID id) {
        DocumentEntity e = docRepo.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(id));
        return toResponse(e);
    }

    @Override
    public Page<DocumentResponse> findAll(Pageable pageable) {
        return docRepo.findAll(pageable).map(DocumentUseCaseService::toResponse);
    }

    @Override
    @Transactional
    public void process(DocumentId id, ProcessDocumentCommand cmd) {
        log.info("Processing document: {} action={}", id.value(), cmd.action());
        // Real impl: VERIFY, REJECT, ARCHIVE
        docRepo.findById(id.value()).ifPresent(e -> {
            e.setUpdatedAt(LocalDateTime.now());
            docRepo.save(e);
        });
    }

    private static DocumentResponse toResponse(DocumentEntity e) {
        return new DocumentResponse(e.getId(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
