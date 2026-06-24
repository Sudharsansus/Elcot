package in.elcot.avgcxr.platform.referencedata.infrastructure.persistence.repository;

import in.elcot.avgcxr.platform.referencedata.infrastructure.persistence.entity.SubspecialtyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaSubspecialtyRepository extends JpaRepository<SubspecialtyEntity, String> {
    List<SubspecialtyEntity> findByOrderByNameEnAsc();
}
