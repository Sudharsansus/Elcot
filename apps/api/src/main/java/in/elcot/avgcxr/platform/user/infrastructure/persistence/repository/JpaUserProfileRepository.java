package in.elcot.avgcxr.platform.user.infrastructure.persistence.repository;

import in.elcot.avgcxr.platform.user.infrastructure.persistence.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface JpaUserProfileRepository extends JpaRepository<UserProfileEntity, UUID> {
}
