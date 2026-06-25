package in.elcot.avgcxr.policy.scheme.infrastructure.persistence.adapter;

import in.elcot.avgcxr.policy.scheme.application.port.output.SchemeRepositoryPort;
import in.elcot.avgcxr.policy.scheme.domain.model.Scheme;
import in.elcot.avgcxr.policy.scheme.infrastructure.persistence.entity.SchemeEntity;
import in.elcot.avgcxr.policy.scheme.infrastructure.persistence.mapper.SchemeMapper;
import in.elcot.avgcxr.policy.scheme.infrastructure.persistence.repository.JpaSchemeRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SchemeRepositoryAdapter implements SchemeRepositoryPort {
  private final JpaSchemeRepository jpaRepository;

  @Override
  public Scheme save(Scheme scheme) {
    return SchemeMapper.toDomain(jpaRepository.save(SchemeMapper.toEntity(scheme)));
  }

  @Override
  public Optional<Scheme> findById(UUID id) {
    return jpaRepository.findById(id).map(SchemeMapper::toDomain);
  }

  @Override
  public Page<Scheme> findAll(String category, String status, String search, Pageable pageable) {
    Page<SchemeEntity> entities = jpaRepository.findPublished(category, search, pageable);
    return entities.map(SchemeMapper::toDomain);
  }
}
