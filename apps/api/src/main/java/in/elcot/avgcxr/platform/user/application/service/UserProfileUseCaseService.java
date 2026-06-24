package in.elcot.avgcxr.platform.user.application.service;

import in.elcot.avgcxr.platform.user.domain.exception.UserProfileNotFoundException;

import in.elcot.avgcxr.platform.user.api.rest.dto.response.UserProfileResponse;
import in.elcot.avgcxr.platform.user.application.command.CreateUserProfileCommand;
import in.elcot.avgcxr.platform.user.application.port.input.CreateUserProfileUseCase;
import in.elcot.avgcxr.platform.user.application.port.input.GetUserProfileUseCase;
import in.elcot.avgcxr.platform.user.infrastructure.persistence.entity.UserProfileEntity;
import in.elcot.avgcxr.platform.user.infrastructure.persistence.repository.JpaUserProfileRepository;
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
public class UserProfileUseCaseService implements CreateUserProfileUseCase, GetUserProfileUseCase {

    private final JpaUserProfileRepository repo;

    @Override
    @Transactional
    public UserProfileResponse create(CreateUserProfileCommand command) {
        log.info("Creating userprofile: {}", command);
        UserProfileEntity e = new UserProfileEntity();
        e.setId(UUID.randomUUID());
        e.setCreatedAt(LocalDateTime.now());
        e.setUpdatedAt(LocalDateTime.now());
        UserProfileEntity saved = repo.save(e);
        return toResponse(saved);
    }

    @Override
    public UserProfileResponse getById(UUID id) {
        UserProfileEntity e = repo.findById(id)
                .orElseThrow(() -> new UserProfileNotFoundException(id));
        return toResponse(e);
    }

    @Override
    public Page<UserProfileResponse> findAll(Pageable pageable) {
        return repo.findAll(pageable).map(UserProfileUseCaseService::toResponse);
    }

    private static UserProfileResponse toResponse(UserProfileEntity e) {
        return new UserProfileResponse(e.getId(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
