package in.elcot.avgcxr.platform.user.infrastructure.persistence.adapter;

import in.elcot.avgcxr.platform.user.application.port.output.UserConsentRepositoryPort;
import in.elcot.avgcxr.platform.user.domain.model.UserConsent;
import in.elcot.avgcxr.platform.user.infrastructure.persistence.entity.UserConsentEntity;
import in.elcot.avgcxr.platform.user.infrastructure.persistence.repository.JpaUserConsentRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class UserConsentPersistenceAdapter implements UserConsentRepositoryPort {

    private final JpaUserConsentRepository repo;

    public UserConsentPersistenceAdapter(JpaUserConsentRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserConsent save(UserConsent c) {
        UserConsentEntity e = new UserConsentEntity();
        e.setId(c.id());
        e.setUserId(c.userId());
        e.setConsentType(c.consentType());
        e.setGranted(c.granted());
        e.setPolicyVersion(c.policyVersion());
        e.setIpAddress(c.ipAddress());
        e.setUserAgent(c.userAgent());
        e.setGrantedAt(c.grantedAt());
        e.setRevokedAt(c.revokedAt());
        if (e.getCreatedAt() == null) e.setCreatedAt(c.grantedAt());
        return toDomain(repo.save(e));
    }

    @Override
    public List<UserConsent> findByUserId(UUID userId) {
        return repo.findByUserId(userId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<UserConsent> findByUserIdAndConsentType(UUID userId, String consentType) {
        return repo.findByUserIdAndConsentType(userId, consentType).stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existsByUserIdAndConsentTypeAndPolicyVersion(UUID userId, String consentType, String policyVersion) {
        return repo.existsByUserIdAndConsentTypeAndPolicyVersion(userId, consentType, policyVersion);
    }

    private UserConsent toDomain(UserConsentEntity e) {
        return new UserConsent(
                e.getId(),
                e.getUserId(),
                e.getConsentType(),
                e.isGranted(),
                e.getPolicyVersion(),
                e.getIpAddress(),
                e.getUserAgent(),
                e.getGrantedAt(),
                e.getRevokedAt()
        );
    }
}