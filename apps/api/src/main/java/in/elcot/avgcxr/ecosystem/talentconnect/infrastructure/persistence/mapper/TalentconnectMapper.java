package in.elcot.avgcxr.ecosystem.talentconnect.infrastructure.persistence.mapper;

import in.elcot.avgcxr.ecosystem.talentconnect.domain.model.Talentconnect;
import in.elcot.avgcxr.ecosystem.talentconnect.domain.model.TalentconnectId;
import in.elcot.avgcxr.ecosystem.talentconnect.infrastructure.persistence.entity.TalentconnectEntity;
import org.springframework.stereotype.Component;

@Component
public class TalentconnectMapper {
  public TalentconnectEntity toEntity(Talentconnect domain) {
    TalentconnectEntity e = new TalentconnectEntity();
    e.setId(domain.getId().toString());
    e.setName("name");
    e.setDescription("description");
    e.setCreatedAt(domain.getCreatedAt());
    e.setUpdatedAt(domain.getUpdatedAt());
    return e;
  }

  public Talentconnect toDomain(TalentconnectEntity entity) {
    return new Talentconnect(
        TalentconnectId.of(entity.getId()), entity.getName() != null ? entity.getName() : "");
  }
}
