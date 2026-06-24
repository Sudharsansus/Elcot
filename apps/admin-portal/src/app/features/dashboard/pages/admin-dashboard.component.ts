import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiClientService } from '@avgc-xr/data-access';
import { CardComponent, ChartComponent } from '@avgc-xr/ui-kit';
import { EChartsOption } from 'echarts';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, CardComponent, ChartComponent],
  templateUrl: './admin-dashboard.component.html',
})
export class AdminDashboardComponent {
  private api = inject(ApiClientService);

  stats = signal({
    totalApps: 0,
    pendingReview: 0,
    approvedToday: 0,
    totalDisbursed: 0,
  });

  barChartOptions: EChartsOption = {
    xAxis: { type: 'category', data: ['Applied', 'Under Review', 'Approved', 'Rejected'] },
    yAxis: { type: 'value' },
    series: [{ type: 'bar', data: [0, 0, 0, 0] }],
  };

  async ngOnInit(): Promise<void> {
    try {
      const res: any = await this.api.get('/dashboard/admin').toPromise();
      if (res) this.stats.set(res);
    } catch (e) {
      console.error('Admin dashboard load failed', e);
    }
  }
}
