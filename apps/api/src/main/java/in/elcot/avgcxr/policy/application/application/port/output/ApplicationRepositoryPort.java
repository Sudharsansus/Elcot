package in.elcot.avgcxr.policy.application.application.port.output;

import in.elcot.avgcxr.policy.application.domain.model.Application;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicationRepositoryPort {
  Application save(Application entity);

  Optional<Application> findById(UUID id);

  Page<Application> findAll(Pageable pageable);

  Page<Application> findByApplicantId(UUID applicantId, Pageable pageable);

  void deleteById(UUID id);
}
