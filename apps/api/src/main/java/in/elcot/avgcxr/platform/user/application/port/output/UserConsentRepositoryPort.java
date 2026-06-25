package in.elcot.avgcxr.platform.user.application.port.output;

import in.elcot.avgcxr.platform.user.domain.model.UserConsent;
import java.util.List;
import java.util.UUID;

/**
 * Output port for user consent records. Implemented by UserConsentPersistenceAdapter in
 * infrastructure/persistence/. The application layer depends only on this interface.
 */
public interface UserConsentRepositoryPort {
  UserConsent save(UserConsent consent);

  List<UserConsent> findByUserId(UUID userId);

  List<UserConsent> findByUserIdAndConsentType(UUID userId, String consentType);

  boolean existsByUserIdAndConsentTypeAndPolicyVersion(
      UUID userId, String consentType, String policyVersion);
}
