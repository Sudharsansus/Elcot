package in.elcot.avgcxr.platform.search.infrastructure.persistence.repository;

import in.elcot.avgcxr.platform.search.infrastructure.persistence.entity.SearchEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSearchRepository extends JpaRepository<SearchEntity, String> {

  /**
   * Full-text search over name + description using PostgreSQL ILIKE. Returns up to {@code limit}
   * rows (capped by caller).
   */
  @Query(
      "SELECT s FROM SearchEntity s "
          + "WHERE LOWER(s.name) LIKE LOWER(:q) "
          + "   OR LOWER(s.description) LIKE LOWER(:q) "
          + "ORDER BY s.updatedAt DESC")
  List<SearchEntity> fullTextSearch(@Param("q") String q, Pageable pageable);

  /** Convenience overload: cap at the given limit. */
  default List<SearchEntity> fullTextSearch(String q, int limit) {
    return fullTextSearch(q, Pageable.ofSize(limit));
  }
}
