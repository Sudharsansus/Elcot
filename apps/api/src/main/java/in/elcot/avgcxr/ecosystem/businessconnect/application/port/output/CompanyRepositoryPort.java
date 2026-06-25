package in.elcot.avgcxr.ecosystem.businessconnect.application.port.output;

import in.elcot.avgcxr.ecosystem.businessconnect.domain.model.Company;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyRepositoryPort {
  Company save(Company entity);

  Optional<Company> findById(UUID id);

  Page<Company> findAll(Pageable pageable);

  void deleteById(UUID id);
}
