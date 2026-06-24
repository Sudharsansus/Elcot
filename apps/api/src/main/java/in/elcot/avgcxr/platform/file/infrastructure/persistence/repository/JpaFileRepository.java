package in.elcot.avgcxr.platform.file.infrastructure.persistence.repository;

import in.elcot.avgcxr.platform.file.infrastructure.persistence.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaFileRepository extends JpaRepository<FileEntity, String> {}
