package in.elcot.avgcxr.platform.referencedata.infrastructure.persistence.repository;

import in.elcot.avgcxr.platform.referencedata.infrastructure.persistence.entity.ReferenceDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaReferenceDataRepository extends JpaRepository<ReferenceDataEntity, UUID> {

    @Query("SELECT r FROM ReferenceDataEntity r WHERE r.category = :category AND r.isActive = true ORDER BY r.sortOrder, r.name")
    List<ReferenceDataEntity> findByCategoryActive(@Param("category") String category);

    @Query("SELECT r FROM ReferenceDataEntity r WHERE r.category = :category ORDER BY r.sortOrder, r.name")
    List<ReferenceDataEntity> findByCategory(@Param("category") String category);
}
