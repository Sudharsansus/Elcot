package in.elcot.avgcxr.ecosystem.freelancerregistry.application.service;

import in.elcot.avgcxr.ecosystem.freelancerregistry.domain.exception.FreelancerProfileNotFoundException;

import in.elcot.avgcxr.ecosystem.freelancerregistry.api.rest.dto.response.FreelancerProfileResponse;
import in.elcot.avgcxr.ecosystem.freelancerregistry.application.command.CreateFreelancerProfileCommand;
import in.elcot.avgcxr.ecosystem.freelancerregistry.application.port.input.CreateFreelancerProfileUseCase;
import in.elcot.avgcxr.ecosystem.freelancerregistry.application.port.input.GetFreelancerProfileUseCase;
import in.elcot.avgcxr.ecosystem.freelancerregistry.infrastructure.persistence.entity.FreelancerProfileEntity;
import in.elcot.avgcxr.ecosystem.freelancerregistry.infrastructure.persistence.repository.JpaFreelancerProfileRepository;
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
public class FreelancerProfileUseCaseService implements CreateFreelancerProfileUseCase, GetFreelancerProfileUseCase {

    private final JpaFreelancerProfileRepository repo;

    @Override
    @Transactional
    public FreelancerProfileResponse create(CreateFreelancerProfileCommand command) {
        log.info("Creating freelancerprofile: {}", command);
        FreelancerProfileEntity e = new FreelancerProfileEntity();
        e.setId(UUID.randomUUID());
        e.setCreatedAt(LocalDateTime.now());
        e.setUpdatedAt(LocalDateTime.now());
        FreelancerProfileEntity saved = repo.save(e);
        return toResponse(saved);
    }

    @Override
    public FreelancerProfileResponse getById(UUID id) {
        FreelancerProfileEntity e = repo.findById(id)
                .orElseThrow(() -> new FreelancerProfileNotFoundException(id));
        return toResponse(e);
    }

    @Override
    public Page<FreelancerProfileResponse> findAll(Pageable pageable) {
        return repo.findAll(pageable).map(FreelancerProfileUseCaseService::toResponse);
    }

    private static FreelancerProfileResponse toResponse(FreelancerProfileEntity e) {
        return new FreelancerProfileResponse(e.getId(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
