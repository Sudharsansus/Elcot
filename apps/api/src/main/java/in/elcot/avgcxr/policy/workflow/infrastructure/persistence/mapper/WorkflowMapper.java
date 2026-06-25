package in.elcot.avgcxr.policy.workflow.infrastructure.persistence.mapper;

import in.elcot.avgcxr.policy.workflow.domain.model.Workflow;
import in.elcot.avgcxr.policy.workflow.domain.model.WorkflowId;
import in.elcot.avgcxr.policy.workflow.infrastructure.persistence.entity.WorkflowEntity;
import org.springframework.stereotype.Component;

@Component
public class WorkflowMapper {
  public WorkflowEntity toEntity(Workflow domain) {
    WorkflowEntity e = new WorkflowEntity();
    e.setId(domain.getId().toString());
    e.setName("name");
    e.setDescription("description");
    e.setCreatedAt(domain.getCreatedAt());
    e.setUpdatedAt(domain.getUpdatedAt());
    return e;
  }

  public Workflow toDomain(WorkflowEntity entity) {
    return new Workflow(
        WorkflowId.of(entity.getId()), entity.getName() != null ? entity.getName() : "");
  }
}
