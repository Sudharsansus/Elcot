package in.elcot.avgcxr.ecosystem.businessconnect.infrastructure.persistence.repository;

import in.elcot.avgcxr.ecosystem.businessconnect.infrastructure.persistence.entity.CompanyEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCompanyRepository extends JpaRepository<CompanyEntity, UUID> {}
