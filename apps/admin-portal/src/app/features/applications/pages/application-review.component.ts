import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiClientService } from '@avgc-xr/data-access';
import { SelectComponent, TableColumn, TableComponent } from '@avgc-xr/ui-kit';
import { AiTriagePanelComponent } from '../ai-triage-panel.component';

@Component({ selector: 'app-application-review', standalone: true, imports: [SelectComponent, CommonModule, TableComponent, AiTriagePanelComponent], templateUrl: './application-review.component.html' })
export class ApplicationReviewComponent {
  private api = inject(ApiClientService);
  columns: TableColumn[] = [
    { key: 'applicationNumber', label: 'App No.', sortable: true },
    { key: 'applicantName', label: 'Applicant' },
    { key: 'schemeName', label: 'Scheme' },
    { key: 'district', label: 'District' },
    { key: 'status', label: 'Status', sortable: true },
  ];
  applications = signal<any[]>([]); loading = signal(false);
  async ngOnInit() { this.loading.set(true); try { const r = await this.api.getPage('/applications', 0, 20, { status: 'UNDER_REVIEW' }).toPromise(); this.applications.set(r?.content ?? []); } finally { this.loading.set(false); } }
}
