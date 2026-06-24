package in.elcot.avgcxr.analytics.dashboard.application.service;

import in.elcot.avgcxr.analytics.dashboard.domain.exception.DashboardDataNotFoundException;

import in.elcot.avgcxr.analytics.dashboard.api.rest.dto.response.DashboardDataResponse;
import in.elcot.avgcxr.analytics.dashboard.application.command.CreateDashboardDataCommand;
import in.elcot.avgcxr.analytics.dashboard.application.port.input.CreateDashboardDataUseCase;
import in.elcot.avgcxr.analytics.dashboard.application.port.input.GetDashboardDataUseCase;
import in.elcot.avgcxr.analytics.dashboard.application.port.output.DashboardDataRepositoryPort;
import in.elcot.avgcxr.analytics.dashboard.domain.model.DashboardData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Real implementation: persists to PostgreSQL via DashboardDataRepositoryPort.
 *
 * <p>HEXAGONAL FIX (HIGH-005 audit): Application layer depends ONLY on the output port.
 * No JPA / Spring Data imports here. The infrastructure layer provides the adapter.</p>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DashboardDataUseCaseService implements CreateDashboardDataUseCase, GetDashboardDataUseCase {

    private final DashboardDataRepositoryPort repo;

    @Override
    @Transactional
    public DashboardDataResponse create(CreateDashboardDataCommand command) {
        log.info("Creating dashboard data: {}", command);
        DashboardData data = DashboardData.builder()
                .id(UUID.randomUUID())
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .build();
        DashboardData saved = repo.save(data);
        return new DashboardDataResponse(saved.getId(), saved.getCreatedAt(), saved.getUpdatedAt());
    }

    @Override
    public DashboardDataResponse getById(UUID id) {
        DashboardData data = repo.findById(id)
                .orElseThrow(() -> new DashboardDataNotFoundException(id));
        return new DashboardDataResponse(data.getId(), data.getCreatedAt(), data.getUpdatedAt());
    }

    @Override
    public Page<DashboardDataResponse> findAll(Pageable pageable) {
        return repo.findAll(pageable)
                .map(d -> new DashboardDataResponse(d.getId(), d.getCreatedAt(), d.getUpdatedAt()));
    }
}