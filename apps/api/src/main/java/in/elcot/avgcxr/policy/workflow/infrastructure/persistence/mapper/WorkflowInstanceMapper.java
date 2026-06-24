package in.elcot.avgcxr.policy.workflow.infrastructure.persistence.mapper;

import in.elcot.avgcxr.policy.workflow.domain.model.WorkflowInstance;
import in.elcot.avgcxr.policy.workflow.infrastructure.persistence.entity.WorkflowInstanceEntity;

public final class WorkflowInstanceMapper {
    private WorkflowInstanceMapper() {}

    public static WorkflowInstance toDomain(WorkflowInstanceEntity e) {
        if (e == null) return null;
        return WorkflowInstance.builder()
                .id(e.getId())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    public static WorkflowInstanceEntity toEntity(WorkflowInstance d) {
        if (d == null) return null;
        WorkflowInstanceEntity e = new WorkflowInstanceEntity();
        e.setId(d.getId());
        e.setCreatedAt(d.getCreatedAt());
        e.setUpdatedAt(d.getUpdatedAt());
        return e;
    }
}
