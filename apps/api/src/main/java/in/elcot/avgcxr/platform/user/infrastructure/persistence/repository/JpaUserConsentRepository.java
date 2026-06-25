package in.elcot.avgcxr.platform.user.infrastructure.persistence.repository;

import in.elcot.avgcxr.platform.user.infrastructure.persistence.entity.UserConsentEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserConsentRepository extends JpaRepository<UserConsentEntity, UUID> {
  List<UserConsentEntity> findByUserId(UUID userId);

  List<UserConsentEntity> findByUserIdAndConsentType(UUID userId, String consentType);

  boolean existsByUserIdAndConsentTypeAndPolicyVersion(
      UUID userId, String consentType, String policyVersion);
}
