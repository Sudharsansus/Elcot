package in.elcot.avgcxr.platform.file.infrastructure.persistence.adapter;

import in.elcot.avgcxr.platform.file.application.port.output.FileRepositoryPort;
import in.elcot.avgcxr.platform.file.domain.model.File;
import in.elcot.avgcxr.platform.file.domain.model.FileId;
import in.elcot.avgcxr.platform.file.infrastructure.persistence.entity.FileEntity;
import in.elcot.avgcxr.platform.file.infrastructure.persistence.mapper.FileMapper;
import in.elcot.avgcxr.platform.file.infrastructure.persistence.repository.JpaFileRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class FileRepositoryAdapter implements FileRepositoryPort {
    private final JpaFileRepository jpaRepo; private final FileMapper mapper;
    public FileRepositoryAdapter(JpaFileRepository jpaRepo, FileMapper mapper) { this.jpaRepo = jpaRepo; this.mapper = mapper; }
    @Override public File save(File f) { return mapper.toDomain(jpaRepo.save(mapper.toEntity(f))); }
    @Override public Optional<File> findById(FileId id) { return jpaRepo.findById(id.toString()).map(mapper::toDomain); }
    @Override public void deleteById(FileId id) { jpaRepo.deleteById(id.toString()); }
}

