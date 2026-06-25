package in.elcot.avgcxr.platform.user.application.service;

import in.elcot.avgcxr.platform.auth.api.rest.dto.response.AuthResponse;
import in.elcot.avgcxr.platform.auth.api.rest.dto.response.UserResponse;
import in.elcot.avgcxr.platform.auth.application.command.UpdateProfileCommand;
import in.elcot.avgcxr.platform.auth.application.port.input.GetUserUseCase;
import in.elcot.avgcxr.platform.user.application.command.CreateUserCommand;
import in.elcot.avgcxr.platform.user.application.port.input.CreateUserUseCase;
import in.elcot.avgcxr.platform.user.application.port.output.UserRepositoryPort;
import in.elcot.avgcxr.platform.user.domain.exception.DuplicateUserException;
import in.elcot.avgcxr.platform.user.domain.exception.UserNotFoundException;
import in.elcot.avgcxr.platform.user.domain.model.User;
import in.elcot.avgcxr.platform.user.domain.model.UserId;
import in.elcot.avgcxr.platformsecurity.jwt.JwtTokenProvider;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService
    implements CreateUserUseCase,
        in.elcot.avgcxr.platform.user.application.port.input.GetUserUseCase,
        GetUserUseCase {
  private final UserRepositoryPort userRepo;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final AccountSecurityService accountSecurity;
  private final PasswordResetService passwordResetService;

  @Override
  @Transactional
  public User create(CreateUserCommand cmd) {
    if (userRepo.existsByEmail(cmd.email())) throw new DuplicateUserException("email", cmd.email());
    if (userRepo.existsByMobileNumber(cmd.mobileNumber()))
      throw new DuplicateUserException("mobile", cmd.mobileNumber());
    User user =
        new User(
            UserId.generate(),
            cmd.username(),
            cmd.email(),
            cmd.mobileNumber(),
            cmd.fullName(),
            cmd.roles());
    user.updateProfile(cmd.fullName(), cmd.fullNameTamil(), cmd.district());
    return userRepo.save(user);
  }

  // ---- in.elcot.avgcxr.platform.user.application.port.input.GetUserUseCase ----
  @Override
  public Optional<User> findById(UserId id) {
    return userRepo.findById(id);
  }

  @Override
  public User getById(UserId id) {
    return userRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id.value()));
  }

  @Override
  public User getByUsername(String username) {
    return userRepo.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
  }

  @Transactional
  public User update(
      UserId id, in.elcot.avgcxr.platform.user.application.command.UpdateUserCommand cmd) {
    User user = getById(id);
    user.updateProfile(cmd.fullName(), cmd.fullNameTamil(), cmd.district());
    return userRepo.save(user);
  }

  // ---- in.elcot.avgcxr.platform.auth.application.port.input.GetUserUseCase ----
  @Override
  public UserResponse getById(UUID id) {
    User u =
        userRepo
            .findById(UserId.of(id.toString()))
            .orElseThrow(() -> new UserNotFoundException(id.toString()));
    return toUserResponse(u);
  }

  @Override
  public UserResponse getCurrentUser() {
    UUID id = getCurrentUserId();
    return getById(id);
  }

  @Override
  public UUID getCurrentUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
      throw new UserNotFoundException("anonymous");
    }
    String name = auth.getName();
    try {
      return UUID.fromString(name);
    } catch (IllegalArgumentException ex) {
      // Authentication.getName() returned a username; look up the user
      return userRepo
          .findByUsername(name)
          .map(u -> u.getId().value())
          .orElseThrow(() -> new UserNotFoundException(name));
    }
  }

  @Override
  @Transactional
  public AuthResponse authenticate(String email, String password) {
    log.info("Login attempt for: {}", email);
    // Look up by email first, then by username
    User user =
        userRepo
            .findByEmail(email)
            .or(() -> userRepo.findByUsername(email))
            .orElseThrow(
                () -> {
                  log.warn("Login failed — user not found: {}", email);
                  return new UserNotFoundException(email);
                });

    // HIGH-011: Check account lockout BEFORE password verification
    if (accountSecurity.isAccountLocked(user)) {
      log.warn("Login rejected — account locked: user={} until={}", email, user.getLockedUntil());
      throw new org.springframework.security.authentication.LockedException(
          "Account is locked until "
              + user.getLockedUntil()
              + " due to too many failed login attempts");
    }

    // Verify password against the bcrypt hash in users.password_hash
    String hash = loadPasswordHash(user.getId().value().toString());
    boolean passwordOk = hash != null && !hash.isBlank() && passwordEncoder.matches(password, hash);
    if (!passwordOk) {
      log.warn("Login failed — bad password for: {}", email);
      // HIGH-011: increment failed-attempt counter, lock if threshold exceeded
      accountSecurity.recordFailedLogin(user);
      throw new org.springframework.security.authentication.BadCredentialsException(
          "Invalid credentials");
    }

    // HIGH-011: successful login resets counter and clears any prior lockout
    User updatedUser = accountSecurity.recordSuccessfulLogin(user);

    String role =
        user.getRoles() != null && !user.getRoles().isEmpty()
            ? user.getRoles().iterator().next()
            : "APPLICANT";
    String accessToken =
        jwtTokenProvider.generateAccessToken(
            user.getId().value().toString(), user.getEmail(), role);
    String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId().value().toString());

    log.info("Login successful: user={} role={}", updatedUser.getId().value(), role);
    return AuthResponse.of(accessToken, refreshToken, 86400L, toUserResponse(updatedUser));
  }

  @Override
  @Transactional
  public AuthResponse refreshToken(String refreshToken) {
    if (refreshToken == null
        || refreshToken.isBlank()
        || !jwtTokenProvider.validateToken(refreshToken)) {
      throw new org.springframework.security.authentication.BadCredentialsException(
          "Invalid refresh token");
    }
    String userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
    User user =
        userRepo.findById(UserId.of(userId)).orElseThrow(() -> new UserNotFoundException(userId));
    String role =
        user.getRoles() != null && !user.getRoles().isEmpty()
            ? user.getRoles().iterator().next()
            : "APPLICANT";
    String newAccess = jwtTokenProvider.generateAccessToken(userId, user.getEmail(), role);
    return AuthResponse.of(newAccess, refreshToken, 86400L, toUserResponse(user));
  }

  @Override
  public UserResponse update(UpdateProfileCommand command) {
    User u =
        userRepo
            .findById(UserId.of(command.userId().toString()))
            .orElseThrow(() -> new UserNotFoundException(command.userId().toString()));
    var req = command.request();
    u.updateProfile(req.fullName(), req.tamilName(), req.district());
    return toUserResponse(userRepo.save(u));
  }

  @Override
  public void requestPasswordReset(String email) {
    if (email == null || email.isBlank()) {
      throw new UserNotFoundException("email required");
    }
    passwordResetService.requestReset(email);
  }

  @Override
  public void resetPassword(String token, String newPassword) {
    passwordResetService.resetPassword(token, newPassword);
  }

  // ---- helpers ----

  @jakarta.persistence.PersistenceContext private jakarta.persistence.EntityManager em;

  private String loadPasswordHash(String userId) {
    try {
      // Use JPQL via the UserEntity so Hibernate binds the UUID type correctly.
      java.util.List<?> results =
          em.createQuery("SELECT u.passwordHash FROM UserEntity u WHERE u.id = :id")
              .setParameter("id", java.util.UUID.fromString(userId))
              .getResultList();
      if (results.isEmpty()) return null;
      Object result = results.get(0);
      return result != null ? result.toString() : null;
    } catch (Exception e) {
      log.error("Could not load password hash for {}: {}", userId, e.getMessage(), e);
      return null;
    }
  }

  private static UserResponse toUserResponse(User u) {
    return new UserResponse(
        u.getId().value(),
        u.getUsername(),
        u.getEmail(),
        u.getMobileNumber(),
        u.getFullName(),
        u.getFullNameTamil(),
        u.getRoles() != null ? new java.util.ArrayList<>(u.getRoles()) : java.util.List.of(),
        u.getStatus() != null ? u.getStatus().name() : "PENDING_VERIFICATION",
        null,
        null,
        u.getDistrict(),
        u.getLastLoginAt() != null
            ? u.getLastLoginAt().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
            : null,
        u.getCreatedAt() != null
            ? u.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
            : null,
        u.getUpdatedAt() != null
            ? u.getUpdatedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
            : null,
        u.isProfileCompleted(),
        null);
  }
}
