package in.elcot.avgcxr.ecosystem.businessconnect.infrastructure.persistence.adapter;

import in.elcot.avgcxr.ecosystem.businessconnect.application.port.output.CompanyRepositoryPort;
import in.elcot.avgcxr.ecosystem.businessconnect.domain.model.Company;
import in.elcot.avgcxr.ecosystem.businessconnect.infrastructure.persistence.mapper.CompanyMapper;
import in.elcot.avgcxr.ecosystem.businessconnect.infrastructure.persistence.repository.JpaCompanyRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CompanyRepositoryAdapter implements CompanyRepositoryPort {
  private final JpaCompanyRepository jpaRepository;

  @Override
  public Company save(Company entity) {
    return CompanyMapper.toDomain(jpaRepository.save(CompanyMapper.toEntity(entity)));
  }

  @Override
  public Optional<Company> findById(UUID id) {
    return jpaRepository.findById(id).map(CompanyMapper::toDomain);
  }

  @Override
  public Page<Company> findAll(Pageable pageable) {
    return jpaRepository.findAll(pageable).map(CompanyMapper::toDomain);
  }

  @Override
  public void deleteById(UUID id) {
    jpaRepository.deleteById(id);
  }
}
