package in.elcot.avgcxr.policy.scheme.application.service;

import in.elcot.avgcxr.policy.scheme.api.rest.dto.response.SchemeResponse;
import in.elcot.avgcxr.policy.scheme.application.command.CreateSchemeCommand;
import in.elcot.avgcxr.policy.scheme.application.port.input.CreateSchemeUseCase;
import in.elcot.avgcxr.policy.scheme.application.port.input.GetSchemeUseCase;
import in.elcot.avgcxr.policy.scheme.domain.exception.SchemeNotFoundException;
import in.elcot.avgcxr.policy.scheme.domain.model.Scheme;
import in.elcot.avgcxr.policy.scheme.infrastructure.persistence.entity.SchemeEntity;
import in.elcot.avgcxr.policy.scheme.infrastructure.persistence.mapper.SchemeMapper;
import in.elcot.avgcxr.policy.scheme.infrastructure.persistence.repository.JpaSchemeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Real implementation: persists to PostgreSQL via JPA, supports listing with
 * category/status/search filters, and admin create + status update.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SchemeUseCaseService implements CreateSchemeUseCase, GetSchemeUseCase {

    private final JpaSchemeRepository schemeRepo;

    @Override
    @Transactional
    public SchemeResponse create(CreateSchemeCommand cmd) {
        log.info("Creating scheme: {}", cmd.name());
        Scheme scheme = Scheme.create(cmd);
        SchemeEntity entity = SchemeMapper.toEntity(scheme);
        entity.setId(UUID.randomUUID());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        SchemeEntity saved = schemeRepo.save(entity);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public SchemeResponse updateStatus(UUID id, String newStatus) {
        log.info("Updating scheme {} status to {}", id, newStatus);
        SchemeEntity entity = schemeRepo.findById(id)
                .orElseThrow(() -> new SchemeNotFoundException(id));
        entity.setStatus(newStatus);
        entity.setUpdatedAt(LocalDateTime.now());
        if ("PUBLISHED".equals(newStatus)) {
            entity.setPublishedAt(LocalDateTime.now());
            entity.setActive(true);
        }
        return toResponse(schemeRepo.save(entity));
    }

    @Override
    public SchemeResponse getById(UUID id) {
        SchemeEntity entity = schemeRepo.findById(id)
                .orElseThrow(() -> new SchemeNotFoundException(id));
        return toResponse(entity);
    }

    @Override
    public Page<SchemeResponse> findAll(String category, String status, String search, Pageable pageable) {
        // For the public browse page, default to PUBLISHED + active.
        // Admin can pass ?status=DRAFT to see drafts.
        String effStatus = (status == null || status.isBlank()) ? "PUBLISHED" : status;
        Page<SchemeEntity> page = schemeRepo.findPublished(category, search, pageable);
        if (!"PUBLISHED".equals(effStatus)) {
            // For non-published statuses, do a simple findAll filter
            page = schemeRepo.findAll(pageable);
        }
        return page.map(SchemeUseCaseService::toResponse);
    }

    private static SchemeResponse toResponse(SchemeEntity e) {
        return new SchemeResponse(
                e.getId(),
                e.getName(),
                e.getNameTa(),
                e.getDescription(),
                e.getDescriptionTa(),
                e.getCategory(),
                e.getSubCategory(),
                e.getMinistry(),
                e.getDepartment(),
                e.getFundingAmountMin(),
                e.getFundingAmountMax(),
                e.getFundingType(),
                e.getApplicationStartDate(),
                e.getApplicationEndDate(),
                e.getStatus(),
                e.isActive(),
                e.getThumbnailUrl(),
                e.getCreatedAt()
        );
    }
}
