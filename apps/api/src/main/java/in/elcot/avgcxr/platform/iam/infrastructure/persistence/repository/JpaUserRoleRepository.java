package in.elcot.avgcxr.platform.iam.infrastructure.persistence.repository;

import in.elcot.avgcxr.platform.iam.infrastructure.persistence.entity.UserRoleEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRoleRepository
    extends JpaRepository<UserRoleEntity, UserRoleEntity.UserRoleId> {

  @Query("SELECT ur FROM UserRoleEntity ur WHERE ur.userId = :userId")
  List<UserRoleEntity> findByUserId(@Param("userId") UUID userId);

  @Query("SELECT ur FROM UserRoleEntity ur WHERE ur.roleId = :roleId")
  List<UserRoleEntity> findByRoleId(@Param("roleId") UUID roleId);
}
