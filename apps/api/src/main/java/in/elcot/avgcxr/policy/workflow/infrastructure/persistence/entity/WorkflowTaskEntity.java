package in.elcot.avgcxr.policy.workflow.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "workflow_tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowTaskEntity {
  @Id private UUID id;

  @Column(name = "process_instance_id")
  private UUID processInstanceId;

  @Column(name = "task_name", length = 255, nullable = false)
  private String taskName;

  @Column(name = "task_name_tamil", length = 300)
  private String taskNameTamil;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "assignee")
  private UUID assignee;

  @JdbcTypeCode(SqlTypes.ARRAY)
  @Column(name = "candidate_groups", columnDefinition = "text[]")
  private String[] candidateGroups;

  @Column(name = "status", length = 30)
  private String status;

  @Column(name = "priority")
  private Integer priority;

  @Column(name = "due_date")
  private LocalDateTime dueDate;

  @Column(name = "completed_at")
  private LocalDateTime completedAt;

  @Column(name = "comment", columnDefinition = "TEXT")
  private String comment;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
}
