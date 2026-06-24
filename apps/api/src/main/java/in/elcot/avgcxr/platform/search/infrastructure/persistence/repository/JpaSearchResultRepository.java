package in.elcot.avgcxr.platform.search.infrastructure.persistence.repository;

import in.elcot.avgcxr.platform.search.infrastructure.persistence.entity.SearchResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface JpaSearchResultRepository extends JpaRepository<SearchResultEntity, UUID> {
}
