package in.elcot.avgcxr.policy.scheme.application.port.output;

import in.elcot.avgcxr.policy.scheme.domain.model.Scheme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface SchemeRepositoryPort {
    Scheme save(Scheme scheme);
    Optional<Scheme> findById(UUID id);
    Page<Scheme> findAll(String category, String status, String search, Pageable pageable);
}
