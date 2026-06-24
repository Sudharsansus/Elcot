package in.elcot.avgcxr.chat.infrastructure.persistence.repository;

import in.elcot.avgcxr.chat.infrastructure.persistence.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaChatMessageRepository extends JpaRepository<ChatMessageEntity, UUID> {

    @Query("SELECT m FROM ChatMessageEntity m WHERE m.sessionId = :sessionId ORDER BY m.createdAt ASC")
    List<ChatMessageEntity> findBySessionId(@Param("sessionId") UUID sessionId);

    @Query("SELECT COUNT(m) FROM ChatMessageEntity m WHERE m.sessionId = :sessionId")
    long countBySessionId(@Param("sessionId") UUID sessionId);

    @Query("SELECT m FROM ChatMessageEntity m WHERE m.sessionId = :sessionId ORDER BY m.createdAt DESC LIMIT :limit")
    List<ChatMessageEntity> findRecentBySessionId(@Param("sessionId") UUID sessionId, @Param("limit") int limit);
}
