package in.elcot.avgcxr.ecosystem.businessconnect.infrastructure.persistence.mapper;

import in.elcot.avgcxr.ecosystem.businessconnect.domain.model.Businessconnect;
import in.elcot.avgcxr.ecosystem.businessconnect.domain.model.BusinessconnectId;
import in.elcot.avgcxr.ecosystem.businessconnect.infrastructure.persistence.entity.BusinessconnectEntity;
import org.springframework.stereotype.Component;

@Component
public class BusinessconnectMapper {
  public BusinessconnectEntity toEntity(Businessconnect domain) {
    BusinessconnectEntity e = new BusinessconnectEntity();
    e.setId(domain.getId().toString());
    e.setName("name");
    e.setDescription("description");
    e.setCreatedAt(domain.getCreatedAt());
    e.setUpdatedAt(domain.getUpdatedAt());
    return e;
  }

  public Businessconnect toDomain(BusinessconnectEntity entity) {
    return new Businessconnect(
        BusinessconnectId.of(entity.getId()), entity.getName() != null ? entity.getName() : "");
  }
}
