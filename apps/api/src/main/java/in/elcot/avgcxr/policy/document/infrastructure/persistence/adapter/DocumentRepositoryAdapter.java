package in.elcot.avgcxr.policy.document.infrastructure.persistence.adapter;

import in.elcot.avgcxr.policy.document.application.port.output.DocumentRepositoryPort;
import in.elcot.avgcxr.policy.document.domain.model.Document;
import in.elcot.avgcxr.policy.document.infrastructure.persistence.entity.DocumentEntity;
import in.elcot.avgcxr.policy.document.infrastructure.persistence.mapper.DocumentMapper;
import in.elcot.avgcxr.policy.document.infrastructure.persistence.repository.JpaDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DocumentRepositoryAdapter implements DocumentRepositoryPort {
    private final JpaDocumentRepository jpaRepository;

    @Override
    public Document save(Document entity) {
        return DocumentMapper.toDomain(jpaRepository.save(DocumentMapper.toEntity(entity)));
    }

    @Override
    public Optional<Document> findById(UUID id) {
        return jpaRepository.findById(id).map(DocumentMapper::toDomain);
    }

    @Override
    public Page<Document> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(DocumentMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
