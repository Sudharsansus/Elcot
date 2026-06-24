package in.elcot.avgcxr.platform.auth.application.service;

import in.elcot.avgcxr.platform.auth.api.rest.dto.response.AuthResponse;
import in.elcot.avgcxr.platform.auth.api.rest.dto.response.UserResponse;
import in.elcot.avgcxr.platform.auth.application.command.RegisterUserCommand;
import in.elcot.avgcxr.platform.auth.application.port.input.CreateUserUseCase;
import in.elcot.avgcxr.platform.user.application.port.output.UserRepositoryPort;
import in.elcot.avgcxr.platform.user.application.service.AccountSecurityService;
import in.elcot.avgcxr.platform.user.domain.exception.DuplicateUserException;
import in.elcot.avgcxr.platform.user.domain.model.User;
import in.elcot.avgcxr.platform.user.domain.model.UserId;
import in.elcot.avgcxr.platformsecurity.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

/**
 * Real implementation: persists new users to PostgreSQL via UserRepositoryPort,
 * hashes the password with BCrypt, and mints a JWT for immediate login.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CreateUserUseCaseService implements CreateUserUseCase {

    private final UserRepositoryPort userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountSecurityService accountSecurity;

    @Override
    public AuthResponse create(RegisterUserCommand cmd) {
        log.info("Registering new user: email={} role={}", cmd.email(), cmd.role());

        if (userRepo.existsByEmail(cmd.email())) {
            throw new DuplicateUserException("email", cmd.email());
        }

        // Derive username from email (frontend sends name/email/phone, not username)
        String username = cmd.email().split("@")[0].toLowerCase();
        if (userRepo.findByUsername(username).isPresent()) {
            for (int i = 1; i < 1000; i++) {
                String candidate = username + i;
                if (userRepo.findByUsername(candidate).isEmpty()) {
                    username = candidate;
                    break;
                }
            }
        }

        // Build the domain User
        Set<String> roles = Set.of(normalizeRole(cmd.role()));
        User user = new User(
                UserId.generate(),
                username,
                cmd.email(),
                cmd.phone(),
                cmd.fullName(),
                roles
        );
        user.updateProfile(cmd.fullName(), cmd.tamilName(), cmd.district());
        user.activate();
        user.completeProfile();

        // Hash password BEFORE saving — UserRepositoryPort.saveWithCredentials writes
        // both the domain user fields AND the bcrypt hash in a single INSERT.
        String passwordHash = passwordEncoder.encode(cmd.rawPassword());

        User saved = userRepo.saveWithCredentials(user, passwordHash);
        UUID userId = saved.getId().value();

        // HIGH-012: Record DPDP consent (mandatory for DPDP Act 2023 compliance).
        // If the frontend doesn't pass an explicit consents map, default to granting
        // DPDP_DATA_PROCESSING (the bare minimum required for the user to use the portal).
        java.util.Map<String, Boolean> consentsToRecord = cmd.consents();
        if (consentsToRecord == null || consentsToRecord.isEmpty()) {
            consentsToRecord = java.util.Map.of(
                    AccountSecurityService.CONSENT_DPDP_DATA_PROCESSING, true);
        }
        accountSecurity.recordConsents(userId, consentsToRecord,
                cmd.ipAddress(), cmd.userAgent());

        log.info("User registered: id={} username={} consents={}", userId, username, consentsToRecord.keySet());

        // Mint JWT
        String accessToken = jwtTokenProvider.generateAccessToken(
                userId.toString(), saved.getEmail(), roles.iterator().next());
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId.toString());

        return AuthResponse.of(accessToken, refreshToken, 86400L, toUserResponse(saved));
    }

    private static String normalizeRole(String role) {
        if (role == null || role.isBlank()) return "APPLICANT";
        String upper = role.toUpperCase().replace('-', '_');
        return switch (upper) {
            case "ADMIN", "SUPER_ADMIN" -> "ADMIN";
            case "OFFICER", "DISTRICT_OFFICER", "NODAL_OFFICER" -> "DISTRICT_OFFICER";
            case "APPLICANT", "USER" -> "APPLICANT";
            default -> "APPLICANT";
        };
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
                null, null, u.getDistrict(),
                null,
                u.getCreatedAt() != null ? u.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime() : null,
                u.getUpdatedAt() != null ? u.getUpdatedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime() : null,
                u.isProfileCompleted(),
                null);
    }
}
