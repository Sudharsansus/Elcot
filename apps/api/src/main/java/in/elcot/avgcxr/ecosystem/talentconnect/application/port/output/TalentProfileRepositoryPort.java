package in.elcot.avgcxr.ecosystem.talentconnect.application.port.output;

import in.elcot.avgcxr.ecosystem.talentconnect.domain.model.TalentProfile;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TalentProfileRepositoryPort {
  TalentProfile save(TalentProfile entity);

  Optional<TalentProfile> findById(UUID id);

  Page<TalentProfile> findAll(Pageable pageable);

  void deleteById(UUID id);
}
