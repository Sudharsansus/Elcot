package in.elcot.avgcxr.ecosystem.bridge.repository;

import in.elcot.avgcxr.ecosystem.bridge.entity.FreelancerRegistryEntity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FreelancerRegistryRepository
    extends JpaRepository<FreelancerRegistryEntity, UUID> {

  @Query(
      "SELECT f FROM FreelancerRegistryEntity f WHERE "
          + "(:district IS NULL OR f.district = :district) AND "
          + "(:specialization IS NULL OR f.specialization = :specialization) AND "
          + "(:q IS NULL OR LOWER(f.fullName) LIKE LOWER(CONCAT('%', CAST(:q AS string), '%')))")
  Page<FreelancerRegistryEntity> search(
      @Param("district") String district,
      @Param("specialization") String specialization,
      @Param("q") String q,
      Pageable pageable);
}
