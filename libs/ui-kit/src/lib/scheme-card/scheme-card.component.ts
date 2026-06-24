import { Component, input, output, ViewEncapsulation } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'avgc-scheme-card',
  standalone: true, imports: [CommonModule],
  templateUrl: './scheme-card.component.html',
  styleUrls: ['./scheme-card.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class SchemeCardComponent {
  name = input<string>('');
  nameTamil = input<string>('');
  description = input<string>('');
  descriptionTamil = input<string>('');
  category = input<string>('');
  maxSubsidy = input<number>(0);
  status = input<string>('');
  startDate = input<string>('');
  endDate = input<string>('');
  totalApplications = input<number>(0);
  viewDetails = output<void>();
  applyNow = output<void>();
}
