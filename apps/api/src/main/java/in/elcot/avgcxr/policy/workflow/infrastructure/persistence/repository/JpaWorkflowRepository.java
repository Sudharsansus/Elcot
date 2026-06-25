package in.elcot.avgcxr.policy.workflow.infrastructure.persistence.repository;

import in.elcot.avgcxr.policy.workflow.infrastructure.persistence.entity.WorkflowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaWorkflowRepository extends JpaRepository<WorkflowEntity, String> {}
