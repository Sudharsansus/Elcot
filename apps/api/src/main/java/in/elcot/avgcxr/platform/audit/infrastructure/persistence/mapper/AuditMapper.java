package in.elcot.avgcxr.platform.audit.infrastructure.persistence.mapper;

import in.elcot.avgcxr.platform.audit.domain.model.Audit;
import in.elcot.avgcxr.platform.audit.domain.model.AuditId;
import in.elcot.avgcxr.platform.audit.infrastructure.persistence.entity.AuditEntity;
import org.springframework.stereotype.Component;

@Component
public class AuditMapper {
  public AuditEntity toEntity(Audit domain) {
    AuditEntity e = new AuditEntity();
    e.setId(domain.getId().toString());
    e.setName("name");
    e.setDescription("description");
    e.setCreatedAt(domain.getCreatedAt());
    e.setUpdatedAt(domain.getUpdatedAt());
    return e;
  }

  public Audit toDomain(AuditEntity entity) {
    return new Audit(AuditId.of(entity.getId()), entity.getName() != null ? entity.getName() : "");
  }
}
