package in.elcot.avgcxr.policy.workflow.infrastructure.persistence.adapter;

import in.elcot.avgcxr.policy.workflow.application.port.output.WorkflowRepositoryPort;
import in.elcot.avgcxr.policy.workflow.domain.model.Workflow;
import in.elcot.avgcxr.policy.workflow.domain.model.WorkflowId;
import in.elcot.avgcxr.policy.workflow.infrastructure.persistence.entity.WorkflowEntity;
import in.elcot.avgcxr.policy.workflow.infrastructure.persistence.mapper.WorkflowMapper;
import in.elcot.avgcxr.policy.workflow.infrastructure.persistence.repository.JpaWorkflowRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class WorkflowRepositoryAdapter implements WorkflowRepositoryPort {

    private final JpaWorkflowRepository jpaRepo;
    private final WorkflowMapper mapper;

    public WorkflowRepositoryAdapter(JpaWorkflowRepository jpaRepo, WorkflowMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    public Workflow save(Workflow entity) { return mapper.toDomain(jpaRepo.save(mapper.toEntity(entity))); }

    @Override
    public Optional<Workflow> findById(WorkflowId id) { return jpaRepo.findById(id.toString()).map(mapper::toDomain); }

    @Override
    public void deleteById(WorkflowId id) { jpaRepo.deleteById(id.toString()); }

    @Override
    public boolean existsById(WorkflowId id) { return jpaRepo.existsById(id.toString()); }
}
