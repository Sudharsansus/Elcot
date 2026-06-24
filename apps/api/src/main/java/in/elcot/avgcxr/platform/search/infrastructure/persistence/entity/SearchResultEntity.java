package in.elcot.avgcxr.platform.search.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "search_results")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SearchResultEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "title", length = 500)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "url", length = 500)
    private String url;

    @Column(name = "score")
    private BigDecimal score;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "entity_id", length = 100)
    private String entityId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
