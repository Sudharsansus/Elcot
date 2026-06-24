import { Component, input, output, computed, ViewEncapsulation } from '@angular/core';
import { CommonModule } from '@angular/common';

export type ButtonVariant = 'primary' | 'secondary' | 'outline' | 'ghost' | 'danger' | 'success';
export type ButtonSize = 'sm' | 'md' | 'lg';

@Component({
  selector: 'avgc-button',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class ButtonComponent {
  variant = input<ButtonVariant>('primary');
  size = input<ButtonSize>('md');
  disabled = input<boolean>(false);
  loading = input<boolean>(false);
  fullWidth = input<boolean>(false);
  type = input<'button' | 'submit' | 'reset'>('button');
  ariaLabel = input<string>('');
  clicked = output<void>();

  classes = computed(() => {
    const v = this.variant();
    const s = this.size();
    return {
      'avgc-btn': true,
      [`avgc-btn--${v}`]: true,
      [`avgc-btn--${s}`]: true,
      'avgc-btn--full-width': this.fullWidth(),
      'avgc-btn--disabled': this.disabled(),
      'avgc-btn--loading': this.loading(),
    };
  });

  onClick(): void {
    if (!this.disabled() && !this.loading()) {
      this.clicked.emit();
    }
  }
}
