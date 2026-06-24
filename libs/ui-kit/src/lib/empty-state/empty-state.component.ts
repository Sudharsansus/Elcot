import { Component, input, ViewEncapsulation, output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'avgc-empty-state',
  standalone: true, imports: [CommonModule],
  templateUrl: './empty-state.component.html',
  styleUrls: ['./empty-state.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class EmptyStateComponent {
  icon = input<string>('inbox');
  title = input<string>('No data found');
  titleTamil = input<string>('');
  description = input<string>('There are no items to display at this time.');
  descriptionTamil = input<string>('');
  actionLabel = input<string>('');
  actionLabelTamil = input<string>('');
  actionClicked = output<void>();
}
