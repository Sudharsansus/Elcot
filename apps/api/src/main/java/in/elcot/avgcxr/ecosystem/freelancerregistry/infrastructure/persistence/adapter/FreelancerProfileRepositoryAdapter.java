package in.elcot.avgcxr.ecosystem.freelancerregistry.infrastructure.persistence.adapter;

import in.elcot.avgcxr.ecosystem.freelancerregistry.application.port.output.FreelancerProfileRepositoryPort;
import in.elcot.avgcxr.ecosystem.freelancerregistry.domain.model.FreelancerProfile;
import in.elcot.avgcxr.ecosystem.freelancerregistry.infrastructure.persistence.entity.FreelancerProfileEntity;
import in.elcot.avgcxr.ecosystem.freelancerregistry.infrastructure.persistence.mapper.FreelancerProfileMapper;
import in.elcot.avgcxr.ecosystem.freelancerregistry.infrastructure.persistence.repository.JpaFreelancerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FreelancerProfileRepositoryAdapter implements FreelancerProfileRepositoryPort {
    private final JpaFreelancerProfileRepository jpaRepository;

    @Override
    public FreelancerProfile save(FreelancerProfile entity) {
        return FreelancerProfileMapper.toDomain(jpaRepository.save(FreelancerProfileMapper.toEntity(entity)));
    }

    @Override
    public Optional<FreelancerProfile> findById(UUID id) {
        return jpaRepository.findById(id).map(FreelancerProfileMapper::toDomain);
    }

    @Override
    public Page<FreelancerProfile> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(FreelancerProfileMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
