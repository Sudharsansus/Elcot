package in.elcot.avgcxr.platform.user.infrastructure.persistence.repository;

import in.elcot.avgcxr.platform.user.infrastructure.persistence.entity.UserProfileEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserProfileRepository extends JpaRepository<UserProfileEntity, UUID> {}
