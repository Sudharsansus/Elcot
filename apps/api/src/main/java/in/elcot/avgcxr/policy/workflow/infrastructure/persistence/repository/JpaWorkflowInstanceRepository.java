package in.elcot.avgcxr.policy.workflow.infrastructure.persistence.repository;

import in.elcot.avgcxr.policy.workflow.infrastructure.persistence.entity.WorkflowInstanceEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaWorkflowInstanceRepository
    extends JpaRepository<WorkflowInstanceEntity, UUID> {}
