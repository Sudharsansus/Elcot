// ============================================================
// SHARED: State Message Component
// ============================================================
// Reusable component for empty, loading, and error states.
// Drop into: apps/public-portal/src/app/shared/state-message/
// ============================================================

import { Component, ChangeDetectionStrategy, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

export type StateVariant = 'loading' | 'empty' | 'error' | 'service-unavailable';

@Component({
  selector: 'app-state-message',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, MatIconModule, MatButtonModule],
  template: `
    <div class="state-message" [class]="'state-' + variant()">
      @if (variant() === 'loading') {
        <div class="state-spinner">
          <div class="spinner-ring"></div>
        </div>
      } @else {
        <div class="state-icon" [style.background]="iconBackground()">
          <mat-icon>{{ iconName() }}</mat-icon>
        </div>
      }

      <h3 class="state-title">{{ title() }}</h3>

      @if (description()) {
        <p class="state-description">{{ description() }}</p>
      }

      @if (actionLabel() && variant() !== 'loading') {
        <button mat-stroked-button color="primary" (click)="action()" class="state-action">
          {{ actionLabel() }}
        </button>
      }
    </div>
  `,
  styles: [`
    :host { display: block; }

    .state-message {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      text-align: center;
      padding: 48px 24px;
      min-height: 280px;
      max-width: 480px;
      margin: 0 auto;
    }

    .state-spinner {
      margin-bottom: 24px;
    }

    .spinner-ring {
      width: 48px;
      height: 48px;
      border: 3px solid var(--color-border);
      border-top-color: var(--color-primary);
      border-radius: 50%;
      animation: spin 0.8s linear infinite;
    }

    @keyframes spin {
      to { transform: rotate(360deg); }
    }

    .state-icon {
      width: 72px;
      height: 72px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 20px;
      background: var(--color-primary-50);
    }

    .state-icon mat-icon {
      font-size: 36px;
      width: 36px;
      height: 36px;
      color: var(--color-primary);
    }

    .state-error .state-icon {
      background: var(--color-error-bg);
    }
    .state-error .state-icon mat-icon {
      color: var(--color-error);
    }

    .state-service-unavailable .state-icon {
      background: var(--color-warning-bg);
    }
    .state-service-unavailable .state-icon mat-icon {
      color: var(--color-warning);
    }

    .state-title {
      font-size: var(--text-xl);
      font-weight: var(--weight-semibold);
      color: var(--color-text-primary);
      margin: 0 0 8px;
      line-height: var(--leading-snug);
    }

    .state-description {
      font-size: var(--text-base);
      color: var(--color-text-secondary);
      margin: 0 0 24px;
      max-width: 360px;
      line-height: var(--leading-relaxed);
    }

    .state-action {
      min-width: 140px;
    }
  `]
})
export class StateMessageComponent {
  variant = input.required<StateVariant>();
  title = input.required<string>();
  description = input<string>('');
  actionLabel = input<string>('');
  action = input<() => void>(() => {});

  iconName(): string {
    switch (this.variant()) {
      case 'empty': return 'inbox';
      case 'error': return 'error_outline';
      case 'service-unavailable': return 'cloud_off';
      default: return 'hourglass_empty';
    }
  }

  iconBackground(): string {
    return ''; // styled via class
  }
}
