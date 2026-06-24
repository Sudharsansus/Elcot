package in.elcot.avgcxr.analytics.dashboard.infrastructure.persistence.adapter;

import in.elcot.avgcxr.analytics.dashboard.application.port.output.DashboardDataRepositoryPort;
import in.elcot.avgcxr.analytics.dashboard.domain.model.DashboardData;
import in.elcot.avgcxr.analytics.dashboard.infrastructure.persistence.entity.DashboardDataEntity;
import in.elcot.avgcxr.analytics.dashboard.infrastructure.persistence.mapper.DashboardDataMapper;
import in.elcot.avgcxr.analytics.dashboard.infrastructure.persistence.repository.JpaDashboardDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DashboardDataRepositoryAdapter implements DashboardDataRepositoryPort {
    private final JpaDashboardDataRepository jpaRepository;

    @Override
    public DashboardData save(DashboardData entity) {
        return DashboardDataMapper.toDomain(jpaRepository.save(DashboardDataMapper.toEntity(entity)));
    }

    @Override
    public Optional<DashboardData> findById(UUID id) {
        return jpaRepository.findById(id).map(DashboardDataMapper::toDomain);
    }

    @Override
    public Page<DashboardData> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(DashboardDataMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
