package in.elcot.avgcxr.ecosystem.businessconnect.infrastructure.persistence.repository;

import in.elcot.avgcxr.ecosystem.businessconnect.infrastructure.persistence.entity.BusinessconnectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaBusinessconnectRepository
    extends JpaRepository<BusinessconnectEntity, String> {}
