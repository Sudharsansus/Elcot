package in.elcot.avgcxr.platform.referencedata.infrastructure.persistence.repository;

import in.elcot.avgcxr.platform.referencedata.infrastructure.persistence.entity.DistrictEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaDistrictRepository extends JpaRepository<DistrictEntity, String> {
  List<DistrictEntity> findByOrderByNameEnAsc();
}
