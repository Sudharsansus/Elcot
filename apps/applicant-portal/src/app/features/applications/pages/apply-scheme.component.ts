import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ApiClientService } from '@avgc-xr/data-access';
import { ButtonComponent, SchemeCardComponent, Step, WorkflowStepperComponent } from '@avgc-xr/ui-kit';

@Component({
  selector: 'app-apply-scheme', standalone: true, imports: [ButtonComponent, WorkflowStepperComponent, CommonModule, SchemeCardComponent],
  templateUrl: './apply-scheme.component.html'
})
export class ApplySchemeComponent {
  readonly steps: Step[] = [{ label: 'Eligibility', status: 'completed' }, { label: 'Application', status: 'active' }, { label: 'Documents', status: 'pending' }, { label: 'Review', status: 'pending' }];
  private route = inject(ActivatedRoute);
  private api = inject(ApiClientService);
  schemeId = this.route.snapshot.params['schemeId'];
  currentStep = 0;
  totalSteps = 5;
  formData: Record<string, any> = {};
  loading = false;

  async ngOnInit() {
    this.loading = true;
    try { const scheme = await this.api.getById('/schemes', this.schemeId).toPromise(); this.formData['scheme'] = scheme; }
    finally { this.loading = false; }
  }

  nextStep() { if (this.currentStep < this.totalSteps) this.currentStep++; }
  prevStep() { if (this.currentStep > 0) this.currentStep--; }
  async submit() { this.loading = true; try { await this.api.post('/applications', this.formData).toPromise(); } finally { this.loading = false; } }
}
