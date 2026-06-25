package in.elcot.avgcxr.ecosystem.talentconnect.infrastructure.persistence.adapter;

import in.elcot.avgcxr.ecosystem.talentconnect.application.port.output.TalentconnectRepositoryPort;
import in.elcot.avgcxr.ecosystem.talentconnect.domain.model.Talentconnect;
import in.elcot.avgcxr.ecosystem.talentconnect.domain.model.TalentconnectId;
import in.elcot.avgcxr.ecosystem.talentconnect.infrastructure.persistence.mapper.TalentconnectMapper;
import in.elcot.avgcxr.ecosystem.talentconnect.infrastructure.persistence.repository.JpaTalentconnectRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class TalentconnectRepositoryAdapter implements TalentconnectRepositoryPort {

  private final JpaTalentconnectRepository jpaRepo;
  private final TalentconnectMapper mapper;

  public TalentconnectRepositoryAdapter(
      JpaTalentconnectRepository jpaRepo, TalentconnectMapper mapper) {
    this.jpaRepo = jpaRepo;
    this.mapper = mapper;
  }

  @Override
  public Talentconnect save(Talentconnect entity) {
    return mapper.toDomain(jpaRepo.save(mapper.toEntity(entity)));
  }

  @Override
  public Optional<Talentconnect> findById(TalentconnectId id) {
    return jpaRepo.findById(id.toString()).map(mapper::toDomain);
  }

  @Override
  public void deleteById(TalentconnectId id) {
    jpaRepo.deleteById(id.toString());
  }

  @Override
  public boolean existsById(TalentconnectId id) {
    return jpaRepo.existsById(id.toString());
  }
}
