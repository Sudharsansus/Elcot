package in.elcot.avgcxr.support.grievance.infrastructure.persistence.adapter;

import in.elcot.avgcxr.support.grievance.application.port.output.GrievanceRepositoryPort;
import in.elcot.avgcxr.support.grievance.domain.model.Grievance;
import in.elcot.avgcxr.support.grievance.infrastructure.persistence.entity.GrievanceEntity;
import in.elcot.avgcxr.support.grievance.infrastructure.persistence.mapper.GrievanceMapper;
import in.elcot.avgcxr.support.grievance.infrastructure.persistence.repository.JpaGrievanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class GrievanceRepositoryAdapter implements GrievanceRepositoryPort {
    private final JpaGrievanceRepository jpaRepository;

    @Override
    public Grievance save(Grievance entity) {
        return GrievanceMapper.toDomain(jpaRepository.save(GrievanceMapper.toEntity(entity)));
    }

    @Override
    public Optional<Grievance> findById(UUID id) {
        return jpaRepository.findById(id).map(GrievanceMapper::toDomain);
    }

    @Override
    public Page<Grievance> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(GrievanceMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
