package in.elcot.avgcxr.ecosystem.businessconnect.application.service;

import in.elcot.avgcxr.ecosystem.businessconnect.domain.exception.CompanyNotFoundException;

import in.elcot.avgcxr.ecosystem.businessconnect.api.rest.dto.response.CompanyResponse;
import in.elcot.avgcxr.ecosystem.businessconnect.application.command.CreateCompanyCommand;
import in.elcot.avgcxr.ecosystem.businessconnect.application.port.input.CreateCompanyUseCase;
import in.elcot.avgcxr.ecosystem.businessconnect.application.port.input.GetCompanyUseCase;
import in.elcot.avgcxr.ecosystem.businessconnect.infrastructure.persistence.entity.CompanyEntity;
import in.elcot.avgcxr.ecosystem.businessconnect.infrastructure.persistence.repository.JpaCompanyRepository;
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
public class CompanyUseCaseService implements CreateCompanyUseCase, GetCompanyUseCase {

    private final JpaCompanyRepository repo;

    @Override
    @Transactional
    public CompanyResponse create(CreateCompanyCommand command) {
        log.info("Creating company: {}", command);
        CompanyEntity e = new CompanyEntity();
        e.setId(UUID.randomUUID());
        e.setCreatedAt(LocalDateTime.now());
        e.setUpdatedAt(LocalDateTime.now());
        CompanyEntity saved = repo.save(e);
        return toResponse(saved);
    }

    @Override
    public CompanyResponse getById(UUID id) {
        CompanyEntity e = repo.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(id));
        return toResponse(e);
    }

    @Override
    public Page<CompanyResponse> findAll(Pageable pageable) {
        return repo.findAll(pageable).map(CompanyUseCaseService::toResponse);
    }

    private static CompanyResponse toResponse(CompanyEntity e) {
        return new CompanyResponse(e.getId(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
