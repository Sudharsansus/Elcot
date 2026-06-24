package in.elcot.avgcxr.platform.iam.infrastructure.persistence.repository;

import in.elcot.avgcxr.platform.iam.infrastructure.persistence.entity.RolePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaRolePermissionRepository
        extends JpaRepository<RolePermissionEntity, RolePermissionEntity.RolePermissionId> {

    @Query("SELECT rp FROM RolePermissionEntity rp WHERE rp.roleId = :roleId")
    List<RolePermissionEntity> findByRoleId(@Param("roleId") UUID roleId);
}
