package in.elcot.avgcxr.platform.user.infrastructure.persistence.repository;

import in.elcot.avgcxr.platform.user.infrastructure.persistence.entity.PasswordResetTokenEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPasswordResetTokenRepository
    extends JpaRepository<PasswordResetTokenEntity, UUID> {

  Optional<PasswordResetTokenEntity> findByTokenHash(String tokenHash);

  @Modifying
  @Query("DELETE FROM PasswordResetTokenEntity t WHERE t.userId = :userId")
  void deleteAllByUserId(@Param("userId") UUID userId);
}
