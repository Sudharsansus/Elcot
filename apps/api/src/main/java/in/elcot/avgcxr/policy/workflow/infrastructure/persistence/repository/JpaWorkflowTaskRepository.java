package in.elcot.avgcxr.policy.workflow.infrastructure.persistence.repository;

import in.elcot.avgcxr.policy.workflow.infrastructure.persistence.entity.WorkflowTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaWorkflowTaskRepository extends JpaRepository<WorkflowTaskEntity, UUID> {

    @Query("SELECT t FROM WorkflowTaskEntity t WHERE t.processInstanceId = :processInstanceId ORDER BY t.createdAt")
    List<WorkflowTaskEntity> findByProcessInstanceId(@Param("processInstanceId") UUID processInstanceId);

    @Query("SELECT t FROM WorkflowTaskEntity t WHERE t.assignee = :assignee AND t.status = 'PENDING'")
    List<WorkflowTaskEntity> findPendingByAssignee(@Param("assignee") UUID assignee);
}
