package in.elcot.avgcxr.policy.application.infrastructure.persistence.adapter;

import in.elcot.avgcxr.policy.application.application.port.output.ApplicationRepositoryPort;
import in.elcot.avgcxr.policy.application.domain.model.Application;
import in.elcot.avgcxr.policy.application.infrastructure.persistence.mapper.ApplicationMapper;
import in.elcot.avgcxr.policy.application.infrastructure.persistence.repository.JpaApplicationRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ApplicationRepositoryAdapter implements ApplicationRepositoryPort {
  private final JpaApplicationRepository jpa;

  @Override
  public Application save(Application a) {
    return ApplicationMapper.toDomain(jpa.save(ApplicationMapper.toEntity(a)));
  }

  @Override
  public Optional<Application> findById(UUID id) {
    return jpa.findById(id).map(ApplicationMapper::toDomain);
  }

  @Override
  public Page<Application> findAll(Pageable pageable) {
    return jpa.findAll(pageable).map(ApplicationMapper::toDomain);
  }

  @Override
  public Page<Application> findByApplicantId(UUID applicantId, Pageable p) {
    return jpa.findByApplicantId(applicantId, p).map(ApplicationMapper::toDomain);
  }

  @Override
  public void deleteById(UUID id) {
    jpa.deleteById(id);
  }
}
