import { Component, input, ViewEncapsulation } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'avgc-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class CardComponent {
  title = input<string>('');
  subtitle = input<string>('');
  elevated = input<boolean>(false);
  hoverable = input<boolean>(false);
  padding = input<'none' | 'sm' | 'md' | 'lg'>('md');
  clickable = input<boolean>(false);
}
