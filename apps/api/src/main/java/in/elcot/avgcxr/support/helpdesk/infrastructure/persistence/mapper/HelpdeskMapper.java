package in.elcot.avgcxr.support.helpdesk.infrastructure.persistence.mapper;

import in.elcot.avgcxr.support.helpdesk.domain.model.Helpdesk;
import in.elcot.avgcxr.support.helpdesk.domain.model.HelpdeskId;
import in.elcot.avgcxr.support.helpdesk.infrastructure.persistence.entity.HelpdeskEntity;
import org.springframework.stereotype.Component;

@Component
public class HelpdeskMapper {
    public HelpdeskEntity toEntity(Helpdesk domain) {
        HelpdeskEntity e = new HelpdeskEntity();
        e.setId(domain.getId().toString());
        e.setName("name");
        e.setDescription("description");
        e.setCreatedAt(domain.getCreatedAt());
        e.setUpdatedAt(domain.getUpdatedAt());
        return e;
    }

    public Helpdesk toDomain(HelpdeskEntity entity) {
        return new Helpdesk(HelpdeskId.of(entity.getId()), entity.getName() != null ? entity.getName() : "");
    }
}
