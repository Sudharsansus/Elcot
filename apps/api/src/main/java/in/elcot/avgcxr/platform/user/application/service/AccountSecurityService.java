package in.elcot.avgcxr.platform.user.application.service;

import in.elcot.avgcxr.platform.user.application.port.output.UserConsentRepositoryPort;
import in.elcot.avgcxr.platform.user.application.port.output.UserRepositoryPort;
import in.elcot.avgcxr.platform.user.domain.model.User;
import in.elcot.avgcxr.platform.user.domain.model.UserConsent;
import in.elcot.avgcxr.platform.user.domain.model.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Account security + DPDP consent management.
 *
 * <p>Brute-force protection (HIGH-011 audit fix): After MAX_FAILED_ATTEMPTS
 * failed logins, the account is locked for LOCKOUT_DURATION_MINUTES.
 * Successful login resets the counter and clears any lockout.</p>
 *
 * <p>DPDP consent (HIGH-012 audit fix): Records explicit consent for data
 * processing under India DPDP Act 2023. Each consent row is immutable;
 * revocation creates a new row with revoked_at set.</p>
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AccountSecurityService {

    private final UserRepositoryPort userRepo;
    private final UserConsentRepositoryPort consentRepo;

    @Value("${security.account-lockout.max-failed-attempts:5}")
    private int maxFailedAttempts;

    @Value("${security.account-lockout.duration-minutes:15}")
    private int lockoutDurationMinutes;

    @Value("${security.dpdp.policy-version:v1.0}")
    private String defaultPolicyVersion;

    public static final String CONSENT_DPDP_DATA_PROCESSING = "DPDP_DATA_PROCESSING";
    public static final String CONSENT_MARKETING = "MARKETING_COMMUNICATIONS";
    public static final String CONSENT_ANALYTICS = "ANALYTICS_TRACKING";

    // ─── Account lockout ────────────────────────────────────────

    /** True if account is locked AND lock hasn't expired. */
    public boolean isAccountLocked(User user) {
        return user.isLocked();
    }

    /**
     * Called from UserService.authenticate() after a failed password check.
     * Increments counter, locks the account if threshold exceeded.
     */
    public User recordFailedLogin(User user) {
        int attempts = user.recordFailedLoginAttempt();
        if (attempts >= maxFailedAttempts) {
            user.lockUntil(Instant.now().plus(Duration.ofMinutes(lockoutDurationMinutes)));
            log.error("Account LOCKED: user={} attempts={} until={}",
                    user.getEmail(), attempts, user.getLockedUntil());
        } else {
            log.warn("Failed login: user={} attempts={}/{}", user.getEmail(), attempts, maxFailedAttempts);
        }
        return userRepo.save(user);
    }

    /** Called from UserService.authenticate() after a successful login. */
    public User recordSuccessfulLogin(User user) {
        if (user.getFailedLoginAttempts() > 0 || user.getLockedUntil() != null) {
            user.resetFailedLogins();
            user.recordLogin();
        } else {
            user.recordLogin();
        }
        return userRepo.save(user);
    }

    /** Admin: manually unlock an account. */
    public User unlockAccount(UserId userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        user.resetFailedLogins();
        User saved = userRepo.save(user);
        log.info("Account manually unlocked: user={}", userId);
        return saved;
    }

    // ─── DPDP consent (HIGH-012) ─────────────────────────────────

    /**
     * Records a consent decision. Returns the persisted record.
     * If a record already exists for (user, type, policy_version) it's a no-op (idempotent).
     */
    public UserConsent recordConsent(UUID userId, String consentType, boolean granted,
                                      String ipAddress, String userAgent) {
        if (!userRepo.findById(UserId.of(userId.toString())).isPresent()) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        if (consentRepo.existsByUserIdAndConsentTypeAndPolicyVersion(userId, consentType, defaultPolicyVersion)) {
            log.debug("Consent already recorded: user={} type={} policy={}", userId, consentType, defaultPolicyVersion);
            return consentRepo.findByUserIdAndConsentType(userId, consentType).stream()
                    .filter(c -> defaultPolicyVersion.equals(c.policyVersion()))
                    .findFirst()
                    .orElseThrow();
        }

        Instant now = Instant.now();
        UserConsent consent = new UserConsent(
                UUID.randomUUID(),
                userId,
                consentType,
                granted,
                defaultPolicyVersion,
                truncate(ipAddress, 64),
                truncate(userAgent, 500),
                now,
                granted ? null : now
        );
        UserConsent saved = consentRepo.save(consent);
        log.info("Consent recorded: user={} type={} granted={} policy={}",
                userId, consentType, granted, defaultPolicyVersion);
        return saved;
    }

    /**
     * Records multiple consents at once (e.g., during registration).
     * Used by CreateUserUseCaseService to honor DPDP checkbox on signup.
     */
    public List<UserConsent> recordConsents(UUID userId, Map<String, Boolean> consents,
                                            String ipAddress, String userAgent) {
        if (consents == null || consents.isEmpty()) {
            return List.of();
        }
        return consents.entrySet().stream()
                .map(e -> recordConsent(userId, e.getKey(),
                        Boolean.TRUE.equals(e.getValue()), ipAddress, userAgent))
                .toList();
    }

    public List<UserConsent> getConsents(UUID userId) {
        return consentRepo.findByUserId(userId);
    }

    public boolean hasActiveConsent(UUID userId, String consentType) {
        return consentRepo.findByUserIdAndConsentType(userId, consentType).stream()
                .anyMatch(UserConsent::isActive);
    }

    private static String truncate(String s, int max) {
        if (s == null) return null;
        return s.length() <= max ? s : s.substring(0, max);
    }
}