import { Component, inject, signal } from '@angular/core';
import { ButtonComponent, CardComponent, EmptyStateComponent } from '@avgc-xr/ui-kit';
import { CommonModule } from '@angular/common';
import { ApiClientService } from '@avgc-xr/data-access';

@Component({ selector: 'app-workflow-manager', standalone: true, imports: [ButtonComponent, CardComponent, EmptyStateComponent, CommonModule], templateUrl: './workflow-manager.component.html' })
export class WorkflowManagerComponent {
  private api = inject(ApiClientService);
  pendingTasks = signal<any[]>([]);
  async ngOnInit() { try { this.pendingTasks.set((await this.api.get<any[]>('/workflows/tasks/pending').toPromise()) ?? []); } catch (e) { console.error(e); } }
  async completeTask(taskId: string, action: string) { await this.api.post('/workflows/tasks/' + taskId + '/complete', { action }).toPromise(); }
}
