/**
 * Workflow Domain Models — BPMN 2.0
 */
export interface WorkflowInstance {
  id: string; processDefinitionId: string; processDefinitionKey: string;
  processDefinitionName: string; businessKey: string; status: WorkflowStatus;
  startedAt: string; completedAt?: string; startedBy: string;
  currentTasks: WorkflowTask[]; variables: Record<string, unknown>;
  history: WorkflowHistoryEntry[];
}
export enum WorkflowStatus { RUNNING='RUNNING', COMPLETED='COMPLETED', SUSPENDED='SUSPENDED', CANCELLED='CANCELLED', FAILED='FAILED' }
export interface WorkflowTask {
  id: string; name: string; nameTamil?: string; description?: string;
  assignee?: string; candidateGroups: string[]; createdAt: string; dueDate?: string;
  priority: number; formKey?: string; category?: string; processInstanceId: string;
  taskDefinitionKey: string;
}
export interface WorkflowAction { taskId: string; action: TaskAction; comment?: string; variables?: Record<string, unknown>; assignee?: string }
export enum TaskAction { COMPLETE='COMPLETE', CLAIM='CLAIM', UNCLAIM='UNCLAIM', DELEGATE='DELEGATE', APPROVE='APPROVE', REJECT='REJECT', REQUEST_INFO='REQUEST_INFO', RETURN_TO_PREVIOUS='RETURN_TO_PREVIOUS' }
export interface WorkflowHistoryEntry { id: string; taskId: string; taskName: string; taskNameTamil?: string; action: string; assignee: string; comment?: string; timestamp: string; duration: number }
export interface WorkflowDashboard { pendingTasksByUser: number; pendingTasksByGroup: number; completedToday: number; completedThisWeek: number; averageProcessingDays: number; applicationsByStatus: Record<string,number>; applicationsByDistrict: Record<string,number>; slaBreached: number; slaApproaching: number }
export interface TaskListRequest { page: number; size: number; assignee?: string; candidateGroup?: string; processDefinitionKey?: string; status?: WorkflowStatus; dueBefore?: string; dueAfter?: string }
export interface WorkflowDiagram { nodes: DiagramNode[]; edges: DiagramEdge[]; currentActivityId?: string }
export interface DiagramNode { id: string; type: string; label: string; labelTamil?: string; x: number; y: number; width: number; height: number }
export interface DiagramEdge { id: string; source: string; target: string; label?: string; labelTamil?: string }
