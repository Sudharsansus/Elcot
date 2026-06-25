package in.elcot.avgcxr.policy.workflow.application.service;

import in.elcot.avgcxr.policy.workflow.application.command.CreateWorkflowCommand;
import in.elcot.avgcxr.policy.workflow.application.command.UpdateWorkflowCommand;
import in.elcot.avgcxr.policy.workflow.application.port.input.CreateWorkflowUseCase;
import in.elcot.avgcxr.policy.workflow.application.port.input.GetWorkflowUseCase;
import in.elcot.avgcxr.policy.workflow.application.port.output.WorkflowRepositoryPort;
import in.elcot.avgcxr.policy.workflow.domain.exception.WorkflowNotFoundException;
import in.elcot.avgcxr.policy.workflow.domain.model.Workflow;
import in.elcot.avgcxr.policy.workflow.domain.model.WorkflowId;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WorkflowService implements CreateWorkflowUseCase, GetWorkflowUseCase {

  private final WorkflowRepositoryPort repository;

  public WorkflowService(WorkflowRepositoryPort repository) {
    this.repository = repository;
  }

  @Override
  @Transactional
  public Workflow create(CreateWorkflowCommand command) {
    Workflow entity = new Workflow(WorkflowId.generate(), command.name());
    return repository.save(entity);
  }

  @Override
  public Optional<Workflow> findById(WorkflowId id) {
    return repository.findById(id);
  }

  @Override
  public Workflow getById(WorkflowId id) {
    return repository.findById(id).orElseThrow(() -> new WorkflowNotFoundException(id.value()));
  }

  @Transactional
  public Workflow update(WorkflowId id, UpdateWorkflowCommand command) {
    Workflow entity = getById(id);
    entity.markUpdated();
    return repository.save(entity);
  }

  @Transactional
  public void delete(WorkflowId id) {
    repository.deleteById(id);
  }
}
