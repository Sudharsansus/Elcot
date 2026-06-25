package in.elcot.avgcxr.policy.workflow.infrastructure.persistence.repository;

import in.elcot.avgcxr.policy.workflow.infrastructure.persistence.entity.WorkflowHistoryEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaWorkflowHistoryRepository extends JpaRepository<WorkflowHistoryEntity, UUID> {

  @Query(
      "SELECT h FROM WorkflowHistoryEntity h WHERE h.workflowInstanceId = :instanceId ORDER BY h.createdAt DESC")
  List<WorkflowHistoryEntity> findByInstanceId(@Param("instanceId") UUID instanceId);
}
