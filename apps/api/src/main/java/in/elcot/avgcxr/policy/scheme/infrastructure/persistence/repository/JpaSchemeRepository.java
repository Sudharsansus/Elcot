package in.elcot.avgcxr.policy.scheme.infrastructure.persistence.repository;

import in.elcot.avgcxr.policy.scheme.infrastructure.persistence.entity.SchemeEntity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSchemeRepository
    extends JpaRepository<SchemeEntity, UUID>, JpaSpecificationExecutor<SchemeEntity> {

  long countByStatus(String status);

  /**
   * Browse published schemes with optional category and search filters. The :search parameter is
   * typed String explicitly to avoid Hibernate binding null as bytea (a known issue with PG driver
   * + nullable JPQL params).
   */
  @Query(
      value =
          "SELECT s FROM SchemeEntity s "
              + "WHERE s.active = true AND s.status = 'PUBLISHED' "
              + "AND (CAST(:category AS string) IS NULL OR s.category = :category) "
              + "AND (CAST(:search AS string) IS NULL OR "
              + "     LOWER(s.name) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')) OR "
              + "     LOWER(s.description) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))) "
              + "ORDER BY s.applicationEndDate ASC")
  Page<SchemeEntity> findPublished(
      @Param("category") String category, @Param("search") String search, Pageable pageable);
}
