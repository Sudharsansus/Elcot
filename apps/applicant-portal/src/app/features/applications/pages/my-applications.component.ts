import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiClientService } from '@avgc-xr/data-access';
import { ButtonComponent, TableColumn, TableComponent } from '@avgc-xr/ui-kit';

@Component({
  selector: 'app-my-applications', standalone: true, imports: [ButtonComponent, CommonModule, TableComponent],
  templateUrl: './my-applications.component.html'
})
export class MyApplicationsComponent implements OnInit {
  protected readonly Math = Math;
  private api = inject(ApiClientService);
  columns: TableColumn[] = [
    { key: 'applicationNumber', label: 'Application No.', sortable: true },
    { key: 'schemeName', label: 'Scheme', sortable: true },
    { key: 'status', label: 'Status', sortable: true },
    { key: 'submissionDate', label: 'Submitted On', sortable: true },
  ];
  applications = signal<any[]>([]);
  loading = signal(false);
  page = 0; size = 10; total = 0;

  async ngOnInit() { await this.loadApplications(); }

  async loadApplications() {
    this.loading.set(true);
    try { const resp = await this.api.getPage('/applications', this.page, this.size).toPromise(); this.applications.set(resp?.content ?? []); this.total = resp?.totalElements ?? 0; }
    finally { this.loading.set(false); }
  }
}
