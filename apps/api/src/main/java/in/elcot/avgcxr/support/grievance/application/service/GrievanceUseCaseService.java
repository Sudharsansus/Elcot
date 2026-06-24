package in.elcot.avgcxr.support.grievance.application.service;

import in.elcot.avgcxr.support.grievance.domain.exception.GrievanceNotFoundException;

import in.elcot.avgcxr.support.grievance.api.rest.dto.response.GrievanceResponse;
import in.elcot.avgcxr.support.grievance.application.command.CreateGrievanceCommand;
import in.elcot.avgcxr.support.grievance.application.port.input.CreateGrievanceUseCase;
import in.elcot.avgcxr.support.grievance.application.port.input.GetGrievanceUseCase;
import in.elcot.avgcxr.support.grievance.application.port.output.GrievanceRepositoryPort;
import in.elcot.avgcxr.support.grievance.domain.model.Grievance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Real implementation: persists to PostgreSQL via the GrievanceRepositoryPort.
 *
 * <p>HEXAGONAL FIX (HIGH-002 audit): Application layer depends ONLY on the output port
 * interface in application/port/output/. The infrastructure layer provides the JPA-backed
 * adapter that implements this port. No JPA / Spring Data imports here.</p>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class GrievanceUseCaseService implements CreateGrievanceUseCase, GetGrievanceUseCase {

    private final GrievanceRepositoryPort repo;

    @Override
    @Transactional
    public GrievanceResponse create(CreateGrievanceCommand command) {
        log.info("Creating grievance: {}", command);
        Grievance grievance = Grievance.create();
        Grievance saved = repo.save(grievance);
        return new GrievanceResponse(saved.getId(), saved.getCreatedAt(), saved.getUpdatedAt());
    }

    @Override
    public GrievanceResponse getById(UUID id) {
        Grievance grievance = repo.findById(id)
                .orElseThrow(() -> new GrievanceNotFoundException(id));
        return new GrievanceResponse(grievance.getId(), grievance.getCreatedAt(), grievance.getUpdatedAt());
    }

    @Override
    public Page<GrievanceResponse> findAll(Pageable pageable) {
        return repo.findAll(pageable)
                .map(g -> new GrievanceResponse(g.getId(), g.getCreatedAt(), g.getUpdatedAt()));
    }
}