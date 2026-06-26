package in.elcot.avgcxr.ecosystem.bridge.repository;

import in.elcot.avgcxr.ecosystem.bridge.entity.BusinessConnectEntity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BusinessConnectRepository extends JpaRepository<BusinessConnectEntity, UUID> {

  @Query(
      "SELECT b FROM BusinessConnectEntity b WHERE "
          + "(:district IS NULL OR b.district = :district) AND "
          + "(:subSector IS NULL OR b.subSector = :subSector) AND "
          + "(:q IS NULL OR LOWER(b.companyName) LIKE LOWER(CONCAT('%', CAST(:q AS string), '%')))")
  Page<BusinessConnectEntity> search(
      @Param("district") String district,
      @Param("subSector") String subSector,
      @Param("q") String q,
      Pageable pageable);
}
