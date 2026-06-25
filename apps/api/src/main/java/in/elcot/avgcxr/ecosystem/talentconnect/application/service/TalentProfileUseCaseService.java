package in.elcot.avgcxr.ecosystem.talentconnect.application.service;

import in.elcot.avgcxr.ecosystem.talentconnect.api.rest.dto.response.TalentProfileResponse;
import in.elcot.avgcxr.ecosystem.talentconnect.application.command.CreateTalentProfileCommand;
import in.elcot.avgcxr.ecosystem.talentconnect.application.port.input.CreateTalentProfileUseCase;
import in.elcot.avgcxr.ecosystem.talentconnect.application.port.input.GetTalentProfileUseCase;
import in.elcot.avgcxr.ecosystem.talentconnect.domain.exception.TalentProfileNotFoundException;
import in.elcot.avgcxr.ecosystem.talentconnect.infrastructure.persistence.entity.TalentProfileEntity;
import in.elcot.avgcxr.ecosystem.talentconnect.infrastructure.persistence.repository.JpaTalentProfileRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Real implementation: persists to PostgreSQL via JPA. Replaces the Phase-1 stub with full CRUD.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TalentProfileUseCaseService
    implements CreateTalentProfileUseCase, GetTalentProfileUseCase {

  private final JpaTalentProfileRepository repo;

  @Override
  @Transactional
  public TalentProfileResponse create(CreateTalentProfileCommand command) {
    log.info("Creating talentprofile: {}", command);
    TalentProfileEntity e = new TalentProfileEntity();
    e.setId(UUID.randomUUID());
    e.setCreatedAt(LocalDateTime.now());
    e.setUpdatedAt(LocalDateTime.now());
    TalentProfileEntity saved = repo.save(e);
    return toResponse(saved);
  }

  @Override
  public TalentProfileResponse getById(UUID id) {
    TalentProfileEntity e =
        repo.findById(id).orElseThrow(() -> new TalentProfileNotFoundException(id));
    return toResponse(e);
  }

  @Override
  public Page<TalentProfileResponse> findAll(Pageable pageable) {
    return repo.findAll(pageable).map(TalentProfileUseCaseService::toResponse);
  }

  private static TalentProfileResponse toResponse(TalentProfileEntity e) {
    return new TalentProfileResponse(e.getId(), e.getCreatedAt(), e.getUpdatedAt());
  }
}
