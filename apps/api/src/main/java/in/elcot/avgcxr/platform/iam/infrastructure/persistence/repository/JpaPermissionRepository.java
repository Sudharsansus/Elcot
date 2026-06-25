package in.elcot.avgcxr.platform.iam.infrastructure.persistence.repository;

import in.elcot.avgcxr.platform.iam.infrastructure.persistence.entity.PermissionEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPermissionRepository extends JpaRepository<PermissionEntity, UUID> {
  Optional<PermissionEntity> findByName(String name);
}
