package in.elcot.avgcxr.policy.workflow.application.command;



public record ProcessWorkflowCommand(String taskId, String action, String comment, String assignee) {}
