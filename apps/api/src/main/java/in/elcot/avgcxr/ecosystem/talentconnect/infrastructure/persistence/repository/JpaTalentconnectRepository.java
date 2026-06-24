package in.elcot.avgcxr.ecosystem.talentconnect.infrastructure.persistence.repository;

import in.elcot.avgcxr.ecosystem.talentconnect.infrastructure.persistence.entity.TalentconnectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTalentconnectRepository extends JpaRepository<TalentconnectEntity, String> {
}
