package in.elcot.avgcxr.platform.iam.infrastructure.persistence.repository;

import in.elcot.avgcxr.platform.iam.infrastructure.persistence.entity.RoleEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaRoleRepository extends JpaRepository<RoleEntity, UUID> {
  Optional<RoleEntity> findByName(String name);
}
