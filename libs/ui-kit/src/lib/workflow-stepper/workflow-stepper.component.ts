import { Component, input, output, ViewEncapsulation } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface Step { label: string; labelTamil?: string; description?: string; status: 'pending' | 'active' | 'completed' | 'error'; }

@Component({
  selector: 'avgc-workflow-stepper',
  standalone: true, imports: [CommonModule],
  templateUrl: './workflow-stepper.component.html',
  styleUrls: ['./workflow-stepper.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class WorkflowStepperComponent {
  steps = input<Step[]>([]);
  currentStep = input<number>(0);
  vertical = input<boolean>(false);
  stepClicked = output<number>();
}
