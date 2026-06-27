// ============================================================
// AUTH SHELL — focused, full-screen split for login / register / reset
// ============================================================
// Renders chrome-free (the app shell hides the marketing nav/footer on
// /auth/* routes). Left: TN-Government brand panel with depth. Right: the
// projected form, centred, with a "Home" link back to the portal.
// ============================================================

import { Component, ChangeDetectionStrategy, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-auth-shell',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, RouterModule, MatIconModule],
  template: `
    <div class="auth-shell">
      <aside class="auth-brand">
        <a routerLink="/" class="auth-brand-home" aria-label="Tamil Nadu AVGC-XR Portal home">
          <span class="ab-emblem"><img src="assets/brand/tn-emblem.png" alt="Government of Tamil Nadu" width="40" height="40" /></span>
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
        <a routerLink="/" class="auth-home-link">
          <mat-icon aria-hidden="true">arrow_back</mat-icon><span>Back to portal</span>
        </a>
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
      grid-template-columns: 1.05fr 1fr;
      min-height: 100vh;
    }

    /* ---- Brand panel (ELCOT deep-purple → violet, matches the site theme) ---- */
    .auth-brand {
      position: relative; overflow: hidden;
      display: flex; flex-direction: column; justify-content: space-between; gap: 32px;
      padding: clamp(32px, 4.5vw, 64px);
      color: #fff;
      background:
        radial-gradient(680px 340px at 88% -6%, rgba(124, 58, 237, 0.55) 0%, transparent 60%),
        linear-gradient(155deg, #45125c 0%, #2c1153 48%, #181233 100%);
    }
    /* subtle dotted texture, fading toward the top-right */
    .auth-brand::before {
      content: ''; position: absolute; inset: 0; pointer-events: none; opacity: 0.55;
      background-image: radial-gradient(rgba(255,255,255,0.12) 1px, transparent 1.4px);
      background-size: 24px 24px;
      -webkit-mask-image: radial-gradient(120% 85% at 100% 0%, #000 0%, transparent 72%);
      mask-image: radial-gradient(120% 85% at 100% 0%, #000 0%, transparent 72%);
    }
    /* soft magenta aurora glow, bottom-left for depth (signature palette) */
    .auth-brand::after {
      content: ''; position: absolute; width: 460px; height: 460px; left: -150px; bottom: -170px;
      background: radial-gradient(circle, rgba(219, 39, 119, 0.38), transparent 66%);
      pointer-events: none;
    }
    .auth-brand > * { position: relative; z-index: 1; }

    .auth-brand-home { display: inline-flex; align-items: center; gap: 14px; color: #fff; text-decoration: none; width: fit-content; }
    .ab-emblem {
      width: 56px; height: 56px; flex-shrink: 0;
      display: grid; place-items: center; border-radius: 50%;
      background: #fff; box-shadow: 0 6px 18px -8px rgba(0,0,0,0.55);
    }
    .ab-emblem img { width: 40px; height: 40px; object-fit: contain; display: block; }
    .ab-home-text { display: flex; flex-direction: column; line-height: 1.2; }
    .ab-gov { font-size: var(--text-sm); color: rgba(255,255,255,0.82); letter-spacing: 0.01em; }
    .ab-portal { font-family: var(--font-serif); font-size: var(--text-lg); font-weight: 700; }

    .ab-title { font-family: var(--font-serif); font-size: clamp(2rem, 3.6vw, 2.9rem); font-weight: 700; line-height: 1.1; margin: 0 0 14px; color: #fff; letter-spacing: -0.01em; }
    .ab-sub { font-size: var(--text-lg); color: rgba(255,255,255,0.86); line-height: 1.6; margin: 0 0 28px; max-width: 440px; }
    .ab-points { list-style: none; padding: 0; margin: 0; display: grid; gap: 14px; }
    .ab-points li { display: flex; align-items: center; gap: 12px; font-size: var(--text-base); color: rgba(255,255,255,0.94); }
    .ab-points mat-icon { color: var(--color-secondary); font-size: 22px; width: 22px; height: 22px; flex-shrink: 0; }
    .auth-brand-foot { font-size: var(--text-sm); color: rgba(255,255,255,0.62); margin: 0; }

    /* ---- Form panel ---- */
    .auth-main {
      position: relative;
      display: flex; align-items: center; justify-content: center;
      padding: clamp(28px, 5vw, 60px);
      background: var(--color-bg);
    }
    .auth-home-link {
      position: absolute; top: clamp(20px, 3vw, 32px); right: clamp(20px, 3vw, 40px);
      display: inline-flex; align-items: center; gap: 6px;
      font-size: var(--text-sm); font-weight: 600; color: var(--ink-mut);
      padding: 8px 14px; border-radius: var(--radius-full);
      border: 1px solid var(--line); background: var(--surface);
      transition: color var(--dur-fast) ease, border-color var(--dur-fast) ease, transform var(--dur-fast) ease;
    }
    .auth-home-link:hover { color: var(--crimson); border-color: var(--glass-border-strong); transform: translateY(-1px); }
    .auth-home-link mat-icon { font-size: 18px; width: 18px; height: 18px; }
    .auth-form-wrap { width: 100%; max-width: 420px; }

    @media (max-width: 900px) {
      .auth-shell { grid-template-columns: 1fr; }
      .auth-brand { padding: 28px; gap: 22px; min-height: auto; }
      .auth-brand-body .ab-points { display: none; }
      .ab-sub { display: none; }
      .auth-brand-foot { display: none; }
      .auth-home-link { top: 16px; right: 16px; }
      .auth-main { padding: 36px 24px 48px; }
    }
  `],
})
export class AuthShellComponent {
  title = input.required<string>();
  subtitle = input<string>('');
  points = input<string[]>([]);
}
