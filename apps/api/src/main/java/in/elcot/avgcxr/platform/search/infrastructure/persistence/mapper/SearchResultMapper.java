package in.elcot.avgcxr.platform.search.infrastructure.persistence.mapper;

import in.elcot.avgcxr.platform.search.domain.model.SearchResult;
import in.elcot.avgcxr.platform.search.infrastructure.persistence.entity.SearchResultEntity;

public final class SearchResultMapper {
    private SearchResultMapper() {}

    public static SearchResult toDomain(SearchResultEntity e) {
        if (e == null) return null;
        return SearchResult.builder()
                .id(e.getId())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    public static SearchResultEntity toEntity(SearchResult d) {
        if (d == null) return null;
        SearchResultEntity e = new SearchResultEntity();
        e.setId(d.getId());
        e.setCreatedAt(d.getCreatedAt());
        e.setUpdatedAt(d.getUpdatedAt());
        return e;
    }
}
