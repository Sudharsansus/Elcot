package in.elcot.avgcxr.ecosystem.talentconnect.infrastructure.persistence.repository;

import in.elcot.avgcxr.ecosystem.talentconnect.infrastructure.persistence.entity.TalentProfileEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTalentProfileRepository extends JpaRepository<TalentProfileEntity, UUID> {}
