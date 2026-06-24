package in.elcot.avgcxr.policy.workflow.api.rest;

import in.elcot.avgcxr.policy.workflow.api.rest.dto.request.CreateWorkflowRequest;
import in.elcot.avgcxr.policy.workflow.api.rest.dto.request.UpdateWorkflowRequest;
import in.elcot.avgcxr.policy.workflow.api.rest.dto.response.WorkflowResponse;
import in.elcot.avgcxr.policy.workflow.application.command.CreateWorkflowCommand;
import in.elcot.avgcxr.policy.workflow.application.command.UpdateWorkflowCommand;
import in.elcot.avgcxr.policy.workflow.application.service.WorkflowService;
import in.elcot.avgcxr.policy.workflow.domain.model.Workflow;
import in.elcot.avgcxr.policy.workflow.domain.model.WorkflowId;
import jakarta.validation.Valid;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/workflows")
public class WorkflowController {

    private final WorkflowService service;
    private final TaskService flowableTaskService;

    public WorkflowController(WorkflowService service, TaskService flowableTaskService) {
        this.service = service;
        this.flowableTaskService = flowableTaskService;
    }

    @PostMapping
    public ResponseEntity<WorkflowResponse> create(@Valid @RequestBody CreateWorkflowRequest request) {
        Workflow entity = service.create(new CreateWorkflowCommand(request.name(), request.description(), "system"));
        return ResponseEntity.created(URI.create("/api/v1/workflows/" + entity.getId()))
            .body(WorkflowResponse.from(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkflowResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(WorkflowResponse.from(service.getById(WorkflowId.of(id))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkflowResponse> update(@PathVariable String id, @Valid @RequestBody UpdateWorkflowRequest request) {
        Workflow entity = service.update(WorkflowId.of(id), new UpdateWorkflowCommand(request.name(), request.description()));
        return ResponseEntity.ok(WorkflowResponse.from(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(WorkflowId.of(id));
        return ResponseEntity.noContent().build();
    }

    // ─── Flowable task endpoints (real TaskService calls) ─────────

    @GetMapping("/tasks/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'DISTRICT_OFFICER', 'NODAL_OFFICER', 'OFFICER')")
    public List<Map<String, Object>> pendingTasks(Authentication auth) {
        String assignee = auth != null ? auth.getName() : null;
        var query = flowableTaskService.createTaskQuery().active();
        if (assignee != null) {
            query = query.taskAssignee(assignee);
        }
        return query.list().stream().map(this::toTaskMap).toList();
    }

    @PostMapping("/tasks/{taskId}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'DISTRICT_OFFICER', 'NODAL_OFFICER', 'OFFICER')")
    public Map<String, Object> completeTask(@PathVariable String taskId,
                                            @RequestBody Map<String, Object> body) {
        flowableTaskService.complete(taskId, body);
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("status", "COMPLETED");
        out.put("taskId", taskId);
        out.put("completedAt", java.time.Instant.now().toString());
        return out;
    }

    @PostMapping("/tasks/{taskId}/claim")
    @PreAuthorize("hasAnyRole('ADMIN', 'DISTRICT_OFFICER', 'NODAL_OFFICER', 'OFFICER')")
    public Map<String, Object> claimTask(@PathVariable String taskId, Authentication auth) {
        flowableTaskService.claim(taskId, auth.getName());
        return Map.of("status", "CLAIMED", "taskId", taskId, "assignee", auth.getName());
    }

    private Map<String, Object> toTaskMap(Task t) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", t.getId());
        m.put("name", t.getName());
        m.put("assignee", t.getAssignee());
        m.put("processInstanceId", t.getProcessInstanceId());
        m.put("taskDefinitionKey", t.getTaskDefinitionKey());
        m.put("createTime", t.getCreateTime() != null ? t.getCreateTime().toInstant().toString() : null);
        return m;
    }
}
