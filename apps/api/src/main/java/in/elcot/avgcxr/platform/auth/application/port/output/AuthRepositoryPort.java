package in.elcot.avgcxr.platform.auth.application.port.output;

import in.elcot.avgcxr.platform.auth.domain.model.Auth;
import java.util.Optional;
import java.util.UUID;

public interface AuthRepositoryPort {
  Auth save(Auth user);

  Optional<Auth> findById(UUID id);

  Optional<Auth> findByEmail(String email);

  boolean existsByEmail(String email);
}
