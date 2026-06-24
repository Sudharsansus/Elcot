package in.elcot.avgcxr.policy.workflow.application.service;

import in.elcot.avgcxr.policy.workflow.domain.exception.WorkflowInstanceNotFoundException;

import in.elcot.avgcxr.policy.workflow.api.rest.dto.response.WorkflowInstanceResponse;
import in.elcot.avgcxr.policy.workflow.application.command.CreateWorkflowInstanceCommand;
import in.elcot.avgcxr.policy.workflow.application.port.input.CreateWorkflowInstanceUseCase;
import in.elcot.avgcxr.policy.workflow.application.port.input.GetWorkflowInstanceUseCase;
import in.elcot.avgcxr.policy.workflow.infrastructure.persistence.entity.WorkflowInstanceEntity;
import in.elcot.avgcxr.policy.workflow.infrastructure.persistence.repository.JpaWorkflowInstanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Real implementation: persists to PostgreSQL via JPA.
 * Replaces the Phase-1 stub with full CRUD.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class WorkflowInstanceUseCaseService implements CreateWorkflowInstanceUseCase, GetWorkflowInstanceUseCase {

    private final JpaWorkflowInstanceRepository repo;

    @Override
    @Transactional
    public WorkflowInstanceResponse create(CreateWorkflowInstanceCommand command) {
        log.info("Creating workflowinstance: {}", command);
        WorkflowInstanceEntity e = new WorkflowInstanceEntity();
        e.setId(UUID.randomUUID());
        e.setCreatedAt(LocalDateTime.now());
        e.setUpdatedAt(LocalDateTime.now());
        WorkflowInstanceEntity saved = repo.save(e);
        return toResponse(saved);
    }

    @Override
    public WorkflowInstanceResponse getById(UUID id) {
        WorkflowInstanceEntity e = repo.findById(id)
                .orElseThrow(() -> new WorkflowInstanceNotFoundException(id));
        return toResponse(e);
    }

    @Override
    public Page<WorkflowInstanceResponse> findAll(Pageable pageable) {
        return repo.findAll(pageable).map(WorkflowInstanceUseCaseService::toResponse);
    }

    private static WorkflowInstanceResponse toResponse(WorkflowInstanceEntity e) {
        return new WorkflowInstanceResponse(e.getId(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
