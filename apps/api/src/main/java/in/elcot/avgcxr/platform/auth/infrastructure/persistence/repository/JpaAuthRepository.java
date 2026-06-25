package in.elcot.avgcxr.platform.auth.infrastructure.persistence.repository;

import in.elcot.avgcxr.platform.auth.infrastructure.persistence.entity.AuthUserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAuthRepository extends JpaRepository<AuthUserEntity, UUID> {

  Optional<AuthUserEntity> findByEmail(String email);

  boolean existsByEmail(String email);

  @Query(
      "SELECT u FROM AuthUserEntity u WHERE "
          + "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR "
          + "LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%')) OR "
          + "u.phone LIKE CONCAT('%', :query, '%')")
  java.util.List<AuthUserEntity> search(@Param("query") String query);
}
