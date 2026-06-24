package in.elcot.avgcxr.policy.application.infrastructure.persistence.repository;

import in.elcot.avgcxr.policy.application.infrastructure.persistence.entity.ApplicationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface JpaApplicationRepository extends JpaRepository<ApplicationEntity, UUID> {

    Page<ApplicationEntity> findByApplicantId(UUID applicantId, Pageable pageable);

    Page<ApplicationEntity> findByApplicantIdAndStatus(UUID applicantId, String status, Pageable pageable);

    @Query("SELECT a FROM ApplicationEntity a WHERE " +
           "(:schemeId IS NULL OR a.schemeId = :schemeId) AND " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:district IS NULL OR a.district = :district) " +
           "ORDER BY a.createdAt DESC")
    Page<ApplicationEntity> findAllFiltered(@Param("schemeId") UUID schemeId,
                                           @Param("status") String status,
                                           @Param("district") String district,
                                           Pageable pageable);

    long countByStatus(String status);
}
