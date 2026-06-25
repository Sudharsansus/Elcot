// ============================================================
// AUTH SHELL — branded split layout for login / register / reset
// ============================================================
// Left: TN-Government brand panel (emblem + trust points). Right: projected
// form. Stacks gracefully on mobile. Keeps the three auth pages consistent.
// ============================================================

import { Component, ChangeDetectionStrategy, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { TnEmblemComponent } from '../../shared/brand/tn-emblem.component';

@Component({
  selector: 'app-auth-shell',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, RouterModule, MatIconModule, TnEmblemComponent],
  template: `
    <div class="auth-shell">
      <aside class="auth-brand">
        <a routerLink="/" class="auth-brand-home" aria-label="Tamil Nadu AVGC-XR Portal home">
          <app-tn-emblem [size]="52" [decorative]="true"></app-tn-emblem>
          <span class="ab-home-text">
            <span class="ab-gov">Government of Tamil Nadu</span>
            <span class="ab-portal">AVGC-XR Portal</span>
          </span>
        </a>

        <div class="auth-brand-body">
          <h1 class="ab-title">{{ title() }}</h1>
          @if (subtitle()) { <p class="ab-sub">{{ subtitle() }}</p> }
          @if (points().length) {
            <ul class="ab-points">
              @for (p of points(); track p) {
                <li><mat-icon aria-hidden="true">check_circle</mat-icon><span>{{ p }}</span></li>
              }
            </ul>
          }
        </div>

        <p class="auth-brand-foot">Electronics Corporation of Tamil Nadu (ELCOT)</p>
      </aside>

      <main class="auth-main">
        <div class="auth-form-wrap">
          <ng-content></ng-content>
        </div>
      </main>
    </div>
  `,
  styles: [`
    :host { display: block; }
    .auth-shell {
      display: grid;
      grid-template-columns: 1fr 1fr;
      min-height: calc(100vh - var(--header-height));
    }
    .auth-brand {
      position: relative;
      display: flex; flex-direction: column; justify-content: space-between; gap: 32px;
      padding: clamp(28px, 5vw, 56px);
      color: #fff;
      background:
        radial-gradient(700px 320px at 85% -10%, #6a0918 0%, transparent 60%),
        linear-gradient(160deg, #9b0c24 0%, #6a0918 55%, #2a0a16 100%);
    }
    .auth-brand-home { display: inline-flex; align-items: center; gap: 14px; color: #fff; text-decoration: none; }
    .auth-brand-home app-tn-emblem { color: var(--color-secondary); }
    .ab-home-text { display: flex; flex-direction: column; line-height: 1.2; }
    .ab-gov { font-size: var(--text-sm); color: rgba(255,255,255,0.8); }
    .ab-portal { font-family: var(--font-serif); font-size: var(--text-lg); font-weight: 700; }
    .ab-title { font-family: var(--font-serif); font-size: clamp(1.8rem, 3.4vw, 2.6rem); font-weight: 700; line-height: 1.12; margin: 0 0 12px; color: #fff; }
    .ab-sub { font-size: var(--text-lg); color: rgba(255,255,255,0.85); line-height: 1.55; margin: 0 0 24px; max-width: 420px; }
    .ab-points { list-style: none; padding: 0; margin: 0; display: grid; gap: 12px; }
    .ab-points li { display: flex; align-items: center; gap: 10px; font-size: var(--text-base); color: rgba(255,255,255,0.92); }
    .ab-points mat-icon { color: var(--color-secondary); font-size: 20px; width: 20px; height: 20px; flex-shrink: 0; }
    .auth-brand-foot { font-size: var(--text-sm); color: rgba(255,255,255,0.65); margin: 0; }

    .auth-main { display: flex; align-items: center; justify-content: center; padding: clamp(24px, 5vw, 56px); background: var(--color-bg); }
    .auth-form-wrap { width: 100%; max-width: 440px; }

    @media (max-width: 860px) {
      .auth-shell { grid-template-columns: 1fr; }
      .auth-brand { padding: 28px; gap: 22px; }
      .auth-brand-body .ab-points { display: none; }
      .ab-sub { display: none; }
      .auth-brand-foot { display: none; }
    }
  `],
})
export class AuthShellComponent {
  title = input.required<string>();
  subtitle = input<string>('');
  points = input<string[]>([]);
}
