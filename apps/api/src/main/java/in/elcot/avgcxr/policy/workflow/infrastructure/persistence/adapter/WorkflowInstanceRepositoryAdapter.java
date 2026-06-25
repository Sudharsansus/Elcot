package in.elcot.avgcxr.policy.workflow.infrastructure.persistence.adapter;

import in.elcot.avgcxr.policy.workflow.application.port.output.WorkflowInstanceRepositoryPort;
import in.elcot.avgcxr.policy.workflow.domain.model.WorkflowInstance;
import in.elcot.avgcxr.policy.workflow.infrastructure.persistence.mapper.WorkflowInstanceMapper;
import in.elcot.avgcxr.policy.workflow.infrastructure.persistence.repository.JpaWorkflowInstanceRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WorkflowInstanceRepositoryAdapter implements WorkflowInstanceRepositoryPort {
  private final JpaWorkflowInstanceRepository jpaRepository;

  @Override
  public WorkflowInstance save(WorkflowInstance entity) {
    return WorkflowInstanceMapper.toDomain(
        jpaRepository.save(WorkflowInstanceMapper.toEntity(entity)));
  }

  @Override
  public Optional<WorkflowInstance> findById(UUID id) {
    return jpaRepository.findById(id).map(WorkflowInstanceMapper::toDomain);
  }

  @Override
  public Page<WorkflowInstance> findAll(Pageable pageable) {
    return jpaRepository.findAll(pageable).map(WorkflowInstanceMapper::toDomain);
  }

  @Override
  public void deleteById(UUID id) {
    jpaRepository.deleteById(id);
  }
}
