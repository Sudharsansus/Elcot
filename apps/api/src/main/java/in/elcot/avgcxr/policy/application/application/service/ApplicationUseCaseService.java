package in.elcot.avgcxr.policy.application.application.service;

import in.elcot.avgcxr.platform.user.application.port.output.UserRepositoryPort;
import in.elcot.avgcxr.policy.application.api.rest.dto.response.ApplicationResponse;
import in.elcot.avgcxr.policy.application.application.command.CreateApplicationCommand;
import in.elcot.avgcxr.policy.application.application.command.ProcessApplicationCommand;
import in.elcot.avgcxr.policy.application.application.port.input.CreateApplicationUseCase;
import in.elcot.avgcxr.policy.application.application.port.input.GetApplicationUseCase;
import in.elcot.avgcxr.policy.application.application.port.input.ProcessApplicationUseCase;
import in.elcot.avgcxr.policy.application.domain.exception.ApplicationNotFoundException;
import in.elcot.avgcxr.policy.application.infrastructure.persistence.entity.ApplicationEntity;
import in.elcot.avgcxr.policy.application.infrastructure.persistence.repository.JpaApplicationRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Real implementation: persists to PostgreSQL via JPA, handles the applicant lifecycle (DRAFT →
 * SUBMITTED → UNDER_REVIEW → APPROVED/REJECTED).
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ApplicationUseCaseService
    implements CreateApplicationUseCase, GetApplicationUseCase, ProcessApplicationUseCase {

  private final JpaApplicationRepository appRepo;
  private final UserRepositoryPort userRepo;

  // ---- CreateApplicationUseCase ----

  @Override
  @Transactional
  public ApplicationResponse create(CreateApplicationCommand cmd) {
    return createInternal(cmd, "DRAFT");
  }

  @Override
  @Transactional
  public ApplicationResponse createDraft(CreateApplicationCommand cmd) {
    return createInternal(cmd, "DRAFT");
  }

  @Override
  @Transactional
  public UUID createReturningId(CreateApplicationCommand cmd) {
    ApplicationResponse r = createInternal(cmd, "DRAFT");
    return r.id();
  }

  private ApplicationResponse createInternal(CreateApplicationCommand cmd, String status) {
    log.info("Creating application: schemeId={} status={}", cmd.schemeId(), status);
    UUID applicantId = currentUserIdOrThrow();

    ApplicationEntity e = new ApplicationEntity();
    e.setId(UUID.randomUUID());
    e.setApplicationNumber(generateApplicationNumber());
    e.setSchemeId(cmd.schemeId());
    e.setApplicantId(applicantId);
    e.setDistrict(cmd.district());
    e.setStatus(status);
    e.setCreatedAt(LocalDateTime.now());
    e.setUpdatedAt(LocalDateTime.now());

    ApplicationEntity saved = appRepo.save(e);
    return toResponse(saved);
  }

  // ---- GetApplicationUseCase ----

  @Override
  public ApplicationResponse getById(UUID id) {
    return toResponse(loadOrThrow(id));
  }

  @Override
  public Page<ApplicationResponse> findAll(Pageable pageable) {
    return appRepo.findAll(pageable).map(ApplicationUseCaseService::toResponse);
  }

  @Override
  public Page<ApplicationResponse> findAll(
      String schemeId, String status, String district, Pageable pageable) {
    UUID sId = (schemeId == null || schemeId.isBlank()) ? null : UUID.fromString(schemeId);
    return appRepo
        .findAllFiltered(sId, status, district, pageable)
        .map(ApplicationUseCaseService::toResponse);
  }

  @Override
  public Page<ApplicationResponse> findMine(UUID applicantId, Pageable pageable) {
    return appRepo
        .findByApplicantId(applicantId, pageable)
        .map(ApplicationUseCaseService::toResponse);
  }

  @Override
  public Page<ApplicationResponse> findByCurrentUser(String status, Pageable pageable) {
    UUID uid = currentUserIdOrThrow();
    if (status == null || status.isBlank()) {
      return appRepo.findByApplicantId(uid, pageable).map(ApplicationUseCaseService::toResponse);
    }
    return appRepo
        .findByApplicantIdAndStatus(uid, status, pageable)
        .map(ApplicationUseCaseService::toResponse);
  }

  // ---- ProcessApplicationUseCase ----

  @Override
  @Transactional
  public ApplicationResponse submit(UUID id) {
    log.info("Submitting application: {}", id);
    ApplicationEntity e = loadOrThrow(id);
    UUID uid = currentUserIdOrThrow();
    if (!e.getApplicantId().equals(uid)) {
      throw new SecurityException("Cannot submit another user's application");
    }
    if (!"DRAFT".equals(e.getStatus())) {
      throw new IllegalStateException(
          "Only DRAFT applications can be submitted (current: " + e.getStatus() + ")");
    }
    e.setStatus("SUBMITTED");
    e.setSubmittedAt(LocalDateTime.now());
    e.setUpdatedAt(LocalDateTime.now());
    return toResponse(appRepo.save(e));
  }

  @Override
  @Transactional
  public ApplicationResponse approve(UUID id, BigDecimal fundingApproved, String remarks) {
    log.info("Approving application: {} funding={}", id, fundingApproved);
    ApplicationEntity e = loadOrThrow(id);
    e.setStatus("APPROVED");
    e.setFundingApproved(fundingApproved);
    e.setReviewedAt(LocalDateTime.now());
    e.setApprovedAt(LocalDateTime.now());
    e.setUpdatedAt(LocalDateTime.now());
    return toResponse(appRepo.save(e));
  }

  @Override
  @Transactional
  public ApplicationResponse reject(UUID id, String reason) {
    log.info("Rejecting application: {} reason={}", id, reason);
    ApplicationEntity e = loadOrThrow(id);
    e.setStatus("REJECTED");
    e.setRejectionReason(reason);
    e.setReviewedAt(LocalDateTime.now());
    e.setRejectedAt(LocalDateTime.now());
    e.setUpdatedAt(LocalDateTime.now());
    return toResponse(appRepo.save(e));
  }

  @Override
  @Transactional
  public ApplicationResponse process(UUID id, ProcessApplicationCommand cmd) {
    log.info("Processing application: {} action={}", id, cmd.action());
    return switch (cmd.action().toLowerCase()) {
      case "submit" -> submit(id);
      case "approve" -> approve(id, BigDecimal.ZERO, cmd.comment());
      case "reject" -> reject(id, cmd.reason());
      case "review" -> markReview(id);
      default -> throw new IllegalArgumentException("Unknown action: " + cmd.action());
    };
  }

  @Transactional
  protected ApplicationResponse markReview(UUID id) {
    ApplicationEntity e = loadOrThrow(id);
    e.setStatus("UNDER_REVIEW");
    e.setUpdatedAt(LocalDateTime.now());
    return toResponse(appRepo.save(e));
  }

  // ---- helpers ----

  private ApplicationEntity loadOrThrow(UUID id) {
    return appRepo.findById(id).orElseThrow(() -> new ApplicationNotFoundException(id));
  }

  private UUID currentUserIdOrThrow() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
      throw new SecurityException("Authentication required");
    }
    String name = auth.getName();
    try {
      return UUID.fromString(name);
    } catch (IllegalArgumentException ex) {
      return userRepo
          .findByUsername(name)
          .map(u -> u.getId().value())
          .orElseThrow(() -> new SecurityException("Unknown user: " + name));
    }
  }

  private String generateApplicationNumber() {
    // Format: AVGC-YYYYMMDD-XXXXXX (random hex)
    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    String random = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    return "AVGC-" + date + "-" + random;
  }

  private static ApplicationResponse toResponse(ApplicationEntity e) {
    return new ApplicationResponse(
        e.getId(),
        e.getApplicationNumber(),
        e.getSchemeId(),
        null, // schemeName — not in entity, would need a join
        e.getApplicantId(),
        null, // applicantName — would need a join
        e.getStatus(),
        e.getDistrict(),
        e.getSubmittedAt(),
        e.getReviewedAt(),
        e.getFundingApproved(),
        e.getRejectionReason());
  }
}
