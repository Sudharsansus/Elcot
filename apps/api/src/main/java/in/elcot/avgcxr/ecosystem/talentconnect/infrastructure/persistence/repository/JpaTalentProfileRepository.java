package in.elcot.avgcxr.ecosystem.talentconnect.infrastructure.persistence.repository;

import in.elcot.avgcxr.ecosystem.talentconnect.infrastructure.persistence.entity.TalentProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface JpaTalentProfileRepository extends JpaRepository<TalentProfileEntity, UUID> {
}
