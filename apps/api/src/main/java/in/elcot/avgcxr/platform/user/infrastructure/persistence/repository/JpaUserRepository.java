package in.elcot.avgcxr.platform.user.infrastructure.persistence.repository;

import in.elcot.avgcxr.platform.user.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, java.util.UUID> {
  java.util.Optional<UserEntity> findByUsername(String username);

  java.util.Optional<UserEntity> findByEmail(String email);

  java.util.Optional<UserEntity> findByMobileNumber(String mobile);

  boolean existsByEmail(String email);

  boolean existsByMobileNumber(String mobile);
}
