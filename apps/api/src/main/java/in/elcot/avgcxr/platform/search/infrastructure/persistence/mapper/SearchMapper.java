package in.elcot.avgcxr.platform.search.infrastructure.persistence.mapper;

import in.elcot.avgcxr.platform.search.domain.model.Search;
import in.elcot.avgcxr.platform.search.domain.model.SearchId;
import in.elcot.avgcxr.platform.search.infrastructure.persistence.entity.SearchEntity;
import org.springframework.stereotype.Component;

@Component
public class SearchMapper {
    public SearchEntity toEntity(Search domain) {
        SearchEntity e = new SearchEntity();
        e.setId(domain.getId().toString());
        e.setName("name");
        e.setDescription("description");
        e.setCreatedAt(domain.getCreatedAt());
        e.setUpdatedAt(domain.getUpdatedAt());
        return e;
    }

    public Search toDomain(SearchEntity entity) {
        return new Search(SearchId.of(entity.getId()), entity.getName() != null ? entity.getName() : "");
    }
}
