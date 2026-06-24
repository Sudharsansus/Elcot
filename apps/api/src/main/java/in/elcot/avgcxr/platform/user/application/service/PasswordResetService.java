package in.elcot.avgcxr.platform.user.application.service;

import in.elcot.avgcxr.common.infrastructure.config.RabbitMQConfig;
import in.elcot.avgcxr.platform.user.domain.exception.InvalidResetTokenException;
import in.elcot.avgcxr.platform.user.infrastructure.persistence.entity.PasswordResetTokenEntity;
import in.elcot.avgcxr.platform.user.infrastructure.persistence.repository.JpaPasswordResetTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Map;
import java.util.UUID;

/**
 * Real password reset flow:
 *  1. requestReset(email) → generate 32-byte token, store SHA-256 hash in DB with 1-hour expiry,
 *     publish reset link to the notification queue
 *  2. resetPassword(token, newPassword) → verify hash + expiry + not-used,
 *     bcrypt-hash new password, UPDATE users.password_hash, mark token used
 */
@Service
public class PasswordResetService {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetService.class);
    private static final long EXPIRY_HOURS = 1;
    private static final SecureRandom RNG = new SecureRandom();

    private final JpaPasswordResetTokenRepository tokenRepo;
    private final ObjectProvider<RabbitTemplate> rabbitTemplateProvider;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager em;

    public PasswordResetService(
            JpaPasswordResetTokenRepository tokenRepo,
            ObjectProvider<RabbitTemplate> rabbitTemplateProvider,
            PasswordEncoder passwordEncoder) {
        this.tokenRepo = tokenRepo;
        this.rabbitTemplateProvider = rabbitTemplateProvider;
        this.passwordEncoder = passwordEncoder;
    }

    private RabbitTemplate rabbitTemplate() {
        return rabbitTemplateProvider.getIfAvailable();
    }

    /**
     * Generate a reset token. If email matches a user, store hashed token in DB and
     * publish reset link to notification queue. Always returns the same shape (no user
     * enumeration leak).
     */
    @Transactional
    public void requestReset(String email) {
        String token = generateToken();
        String tokenHash = sha256Hex(token);
        Instant now = Instant.now();
        Instant expires = now.plusSeconds(EXPIRY_HOURS * 3600);

        // Look up user id by email (no enumeration: only persist token if found)
        UUID userId = null;
        try {
            @SuppressWarnings("unchecked")
            java.util.List<UUID> ids = em.createQuery(
                    "SELECT u.id FROM UserEntity u WHERE u.email = :email")
                    .setParameter("email", email)
                    .getResultList();
            if (!ids.isEmpty()) userId = ids.get(0);
        } catch (Exception e) {
            log.error("Email lookup failed for reset: {}", e.getMessage());
        }

        if (userId != null) {
            // Invalidate any prior tokens for this user
            tokenRepo.deleteAllByUserId(userId);

            PasswordResetTokenEntity entity = PasswordResetTokenEntity.builder()
                    .id(UUID.randomUUID())
                    .userId(userId)
                    .tokenHash(tokenHash)
                    .expiresAt(expires)
                    .createdAt(now)
                    .build();
            tokenRepo.save(entity);

            // Publish reset link to notification queue (best-effort: if RabbitMQ is not available,
            // log the reset link so developers can still test the flow)
            String resetLink = "/reset-password?token=" + token;
            Map<String, Object> event = Map.of(
                    "eventType", "PASSWORD_RESET_REQUESTED",
                    "userId", userId.toString(),
                    "email", email,
                    "channel", "EMAIL",
                    "subject", "Password reset requested",
                    "body", "Click the link to reset your password: " + resetLink
                            + "\n\nThis link expires in " + EXPIRY_HOURS + " hour.",
                    "resetToken", token,
                    "expiresAt", expires.toString()
            );
            RabbitTemplate rt = rabbitTemplate();
            if (rt != null) {
                rt.convertAndSend(
                        RabbitMQConfig.EXCHANGE,
                        RabbitMQConfig.RK_NOTIFICATION_DISPATCH,
                        event
                );
            } else {
                log.info("[DEV ONLY — no RabbitMQ] Password reset link: {}", resetLink);
            }
            log.info("Password reset token issued for user={} expiresAt={}", userId, expires);
        } else {
            // User not found — log only, do not send any notification (no enumeration)
            log.info("Password reset requested for unknown email: {}", email);
        }
    }

    /**
     * Verify token, hash new password, UPDATE users.password_hash, mark token used.
     */
    @Transactional
    public void resetPassword(String token, String newPassword) {
        if (token == null || token.isBlank() || newPassword == null || newPassword.length() < 8) {
            throw new InvalidResetTokenException("Invalid token or weak password");
        }
        String tokenHash = sha256Hex(token);
        PasswordResetTokenEntity t = tokenRepo.findByTokenHash(tokenHash)
                .orElseThrow(() -> new InvalidResetTokenException("Token not found"));

        if (t.getUsedAt() != null) {
            throw new InvalidResetTokenException("Token already used");
        }
        if (t.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidResetTokenException("Token expired");
        }

        // Update password
        String hashed = passwordEncoder.encode(newPassword);
        int updated = em.createQuery(
                "UPDATE UserEntity u SET u.passwordHash = :hash WHERE u.id = :id")
                .setParameter("hash", hashed)
                .setParameter("id", t.getUserId())
                .executeUpdate();
        if (updated == 0) {
            throw new InvalidResetTokenException("User not found for token");
        }

        t.setUsedAt(Instant.now());
        tokenRepo.save(t);

        log.info("Password reset completed for user={}", t.getUserId());
    }

    // ---- helpers ----
    private static String generateToken() {
        byte[] buf = new byte[32];
        RNG.nextBytes(buf);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
    }

    private static String sha256Hex(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(s.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (Exception e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
