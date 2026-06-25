package in.elcot.avgcxr.chat.infrastructure.persistence.repository;

import in.elcot.avgcxr.chat.infrastructure.persistence.entity.ChatSessionEntity;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaChatSessionRepository extends JpaRepository<ChatSessionEntity, UUID> {

  Optional<ChatSessionEntity> findBySessionToken(String sessionToken);

  @Query("SELECT s FROM ChatSessionEntity s WHERE s.userId = :userId ORDER BY s.updatedAt DESC")
  List<ChatSessionEntity> findByUserId(@Param("userId") UUID userId);

  @Query("SELECT s FROM ChatSessionEntity s WHERE s.isActive = true AND s.expiresAt < :now")
  List<ChatSessionEntity> findExpired(@Param("now") Instant now);

  @Modifying
  @Query("DELETE FROM ChatSessionEntity s WHERE s.expiresAt < :now")
  int deleteExpired(@Param("now") Instant now);
}
