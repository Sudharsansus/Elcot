package in.elcot.avgcxr.ecosystem.bridge.repository;

import in.elcot.avgcxr.ecosystem.bridge.entity.TalentConnectEntity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TalentConnectRepository extends JpaRepository<TalentConnectEntity, UUID> {

  @Query(
      "SELECT t FROM TalentConnectEntity t WHERE "
          + "(:district IS NULL OR t.district = :district) AND "
          + "(:subSector IS NULL OR t.subSector = :subSector) AND "
          + "(:q IS NULL OR LOWER(t.fullName) LIKE LOWER(CONCAT('%', CAST(:q AS string), '%')))")
  Page<TalentConnectEntity> search(
      @Param("district") String district,
      @Param("subSector") String subSector,
      @Param("q") String q,
      Pageable pageable);
}
