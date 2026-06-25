package in.elcot.avgcxr.ecosystem.talentconnect.infrastructure.persistence.adapter;

import in.elcot.avgcxr.ecosystem.talentconnect.application.port.output.TalentProfileRepositoryPort;
import in.elcot.avgcxr.ecosystem.talentconnect.domain.model.TalentProfile;
import in.elcot.avgcxr.ecosystem.talentconnect.infrastructure.persistence.mapper.TalentProfileMapper;
import in.elcot.avgcxr.ecosystem.talentconnect.infrastructure.persistence.repository.JpaTalentProfileRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TalentProfileRepositoryAdapter implements TalentProfileRepositoryPort {
  private final JpaTalentProfileRepository jpaRepository;

  @Override
  public TalentProfile save(TalentProfile entity) {
    return TalentProfileMapper.toDomain(jpaRepository.save(TalentProfileMapper.toEntity(entity)));
  }

  @Override
  public Optional<TalentProfile> findById(UUID id) {
    return jpaRepository.findById(id).map(TalentProfileMapper::toDomain);
  }

  @Override
  public Page<TalentProfile> findAll(Pageable pageable) {
    return jpaRepository.findAll(pageable).map(TalentProfileMapper::toDomain);
  }

  @Override
  public void deleteById(UUID id) {
    jpaRepository.deleteById(id);
  }
}
