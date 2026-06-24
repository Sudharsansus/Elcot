package in.elcot.avgcxr.ecosystem.businessconnect.application.port.output;

import in.elcot.avgcxr.ecosystem.businessconnect.domain.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepositoryPort {
    Company save(Company entity);
    Optional<Company> findById(UUID id);
    Page<Company> findAll(Pageable pageable);
    void deleteById(UUID id);
}
