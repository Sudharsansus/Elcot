import { Component, inject, signal } from '@angular/core';
import { CardComponent } from '@avgc-xr/ui-kit';
import { CommonModule } from '@angular/common';
import { ApiClientService } from '@avgc-xr/data-access';
import { ApplicantAiComponent } from '../applicant-ai.component';

@Component({
  selector: 'app-applicant-dashboard', standalone: true, imports: [CardComponent, CommonModule, ApplicantAiComponent],
  templateUrl: './applicant-dashboard.component.html'
})
export class ApplicantDashboardComponent {
  private api = inject(ApiClientService);
  stats = signal({ total: 0, approved: 0, pending: 0, rejected: 0 });

  async ngOnInit() {
    try { const data = await this.api.get('/dashboard/applicant').toPromise(); this.stats.set(data as { total: number; approved: number; pending: number; rejected: number }); } catch (e) { console.error('Failed to load dashboard', e); }
  }
}
