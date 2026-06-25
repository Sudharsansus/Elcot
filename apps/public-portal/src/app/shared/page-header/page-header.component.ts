// ============================================================
// PAGE HEADER — reusable institutional sub-masthead for interior pages
// ============================================================
// Gives every interior route a consistent TN-Gov header band: breadcrumb,
// serif title, optional bilingual subtitle and icon. Keeps the portal coherent
// and makes building the remaining pages fast.
// ============================================================

import { Component, ChangeDetectionStrategy, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

export interface Crumb { label: string; route?: string; }

@Component({
  selector: 'app-page-header',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, RouterModule, MatIconModule],
  template: `
    <header class="page-header">
      <div class="ph-accent" aria-hidden="true"></div>
      <div class="container">
        <nav class="ph-crumbs" aria-label="Breadcrumb">
          <a routerLink="/">{{ homeLabel() }}</a>
          @for (c of breadcrumb(); track c.label; let last = $last) {
            <mat-icon aria-hidden="true">chevron_right</mat-icon>
            @if (c.route && !last) {
              <a [routerLink]="c.route">{{ c.label }}</a>
            } @else {
              <span aria-current="page">{{ c.label }}</span>
            }
          }
        </nav>

        <div class="ph-main">
          @if (icon()) {
            <div class="ph-icon" aria-hidden="true"><mat-icon>{{ icon() }}</mat-icon></div>
          }
          <div class="ph-text">
            <h1 class="ph-title">{{ title() }}</h1>
            @if (subtitle()) { <p class="ph-subtitle">{{ subtitle() }}</p> }
          </div>
        </div>
      </div>
    </header>
  `,
  styles: [`
    :host { display: block; }
    .page-header {
      position: relative;
      background:
        radial-gradient(700px 300px at 90% -40%, rgba(200,16,46,0.06), transparent 60%),
        var(--color-bg-subtle);
      border-bottom: 1px solid var(--color-border);
      padding: clamp(24px, 4vw, 44px) 0 clamp(20px, 3vw, 32px);
      overflow: hidden;
    }
    .ph-accent {
      position: absolute; top: 0; left: 0; right: 0; height: 4px;
      background: linear-gradient(90deg,
        var(--color-primary) 0 33%, var(--color-secondary) 33% 66%, var(--color-primary) 66% 100%);
    }
    .ph-crumbs {
      display: flex; align-items: center; flex-wrap: wrap; gap: 4px;
      font-size: var(--text-sm); color: var(--color-text-tertiary);
      margin-bottom: 14px;
    }
    .ph-crumbs a { color: var(--color-text-secondary); text-decoration: none; }
    .ph-crumbs a:hover { color: var(--color-primary); text-decoration: underline; }
    .ph-crumbs span { color: var(--color-text-primary); font-weight: 600; }
    .ph-crumbs mat-icon { font-size: 16px; width: 16px; height: 16px; color: var(--color-text-disabled); }
    .ph-main { display: flex; align-items: center; gap: 18px; }
    .ph-icon {
      flex-shrink: 0;
      width: 60px; height: 60px;
      display: inline-flex; align-items: center; justify-content: center;
      background: var(--color-primary); color: #fff;
      border-radius: var(--radius-lg);
      box-shadow: 0 6px 16px color-mix(in srgb, var(--color-primary) 28%, transparent);
    }
    .ph-icon mat-icon { font-size: 30px; width: 30px; height: 30px; }
    .ph-title {
      font-family: var(--font-serif);
      font-size: clamp(1.6rem, 3.6vw, 2.4rem);
      font-weight: 700; line-height: 1.12;
      color: var(--color-text-primary); margin: 0;
    }
    .ph-subtitle {
      font-size: var(--text-base); color: var(--color-text-secondary);
      line-height: 1.55; margin: 6px 0 0; max-width: 720px;
    }
    @media (max-width: 600px) {
      .ph-icon { width: 48px; height: 48px; }
      .ph-icon mat-icon { font-size: 24px; width: 24px; height: 24px; }
    }
  `],
})
export class PageHeaderComponent {
  title = input.required<string>();
  subtitle = input<string>('');
  icon = input<string>('');
  breadcrumb = input<Crumb[]>([]);
  homeLabel = input<string>('Home');
}
