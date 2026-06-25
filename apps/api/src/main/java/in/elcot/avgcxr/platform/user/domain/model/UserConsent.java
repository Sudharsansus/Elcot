package in.elcot.avgcxr.platform.user.domain.model;

import java.time.Instant;
import java.util.UUID;

/**
 * Immutable DPDP consent record for one user × consent-type × policy-version. Once granted/revoked,
 * the row is never updated; new decisions create new rows.
 *
 * <p>Stored in user_consents table per V24 migration. Required for DPDP Act 2023 compliance
 * (Sections 6, 7, 8 — consent for data processing).
 */
public record UserConsent(
    UUID id,
    UUID userId,
    String consentType, // DPDP_DATA_PROCESSING, MARKETING_COMMUNICATIONS, etc.
    boolean granted,
    String policyVersion, // "v1.0", "v2.1" — bump to require re-consent
    String ipAddress,
    String userAgent,
    Instant grantedAt,
    Instant revokedAt) {
  public UserConsent {
    if (consentType == null || consentType.isBlank()) {
      throw new IllegalArgumentException("consentType is required");
    }
    if (policyVersion == null || policyVersion.isBlank()) {
      throw new IllegalArgumentException("policyVersion is required");
    }
  }

  public boolean isActive() {
    return granted && revokedAt == null;
  }
}
