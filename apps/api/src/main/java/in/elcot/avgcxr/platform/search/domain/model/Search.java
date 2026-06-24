package in.elcot.avgcxr.platform.search.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Search {
    private final SearchId id;
    private String name;
    private String description;

    private final Instant createdAt;
    private Instant updatedAt;

    public Search(SearchId id, String name) {
        this.id = id;
        this.name = name;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Search(SearchId id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public SearchId getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void markUpdated() { this.updatedAt = Instant.now(); }
}
