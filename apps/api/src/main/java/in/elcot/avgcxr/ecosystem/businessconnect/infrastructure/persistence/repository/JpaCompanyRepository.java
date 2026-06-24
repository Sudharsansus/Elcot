package in.elcot.avgcxr.ecosystem.businessconnect.infrastructure.persistence.repository;

import in.elcot.avgcxr.ecosystem.businessconnect.infrastructure.persistence.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface JpaCompanyRepository extends JpaRepository<CompanyEntity, UUID> {
}
