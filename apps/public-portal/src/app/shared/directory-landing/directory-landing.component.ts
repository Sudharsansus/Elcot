// ============================================================
// DIRECTORY LANDING — honest shell for registry pages
// ============================================================
// Companies / Talent / Freelancers have no seeded data in the repo. Rather than
// fabricate listings, this presents the REAL purpose of each registry, what a
// visitor will find once organisations/people register, and a clear call to
// register — plus an honest status note that listings appear as entries are
// verified. Presentational only; the bilingual copy is supplied by the wrapper.
// ============================================================

import { Component, ChangeDetectionStrategy, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { PageHeaderComponent, Crumb } from '../page-header/page-header.component';
import { RevealDirective } from '../directives/reveal.directive';

export interface DirectoryFeature { icon: string; title: string; desc: string; }

@Component({
  selector: 'app-directory-landing',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, RouterModule, MatIconModule, PageHeaderComponent, RevealDirective],
  template: `
    <app-page-header
      [title]="title()"
      [subtitle]="subtitle()"
      [icon]="icon()"
      [breadcrumb]="breadcrumb()"
      [homeLabel]="homeLabel()">
    </app-page-header>

    <section class="dir">
      <div class="container">
        <!-- honest status banner -->
        <div class="dir-status" role="status" appReveal="fade">
          <mat-icon aria-hidden="true">info</mat-icon>
          <p>{{ statusNote() }}</p>
        </div>

        @if (intro()) { <p class="dir-intro" appReveal="up">{{ intro() }}</p> }

        @if (features().length) {
          <div class="dir-features">
            @for (f of features(); track f.title; let i = $index) {
              <article class="dir-feature" appReveal="up" [revealDelay]="i * 70">
                <div class="dir-feature-icon"><mat-icon aria-hidden="true">{{ f.icon }}</mat-icon></div>
                <h3>{{ f.title }}</h3>
                <p>{{ f.desc }}</p>
              </article>
            }
          </div>
        }

        <div class="dir-cta" appReveal="scale">
          <div class="dir-cta-text">
            <h2>{{ ctaTitle() }}</h2>
            <p>{{ ctaText() }}</p>
          </div>
          <a [routerLink]="ctaRoute()" class="dir-cta-btn">
            <mat-icon aria-hidden="true">{{ ctaIcon() }}</mat-icon>
            {{ ctaLabel() }}
          </a>
        </div>
      </div>
    </section>
  `,
  styles: [`
    :host { display: block; }
    .dir { padding: clamp(28px, 5vw, 56px) 0 clamp(48px, 7vw, 80px); }
    .dir-status {
      display: flex; align-items: center; gap: 12px;
      padding: 14px 18px; margin-bottom: 28px;
      background: var(--color-info-bg); border-left: 4px solid var(--color-info);
      border-radius: var(--radius-md);
    }
    .dir-status mat-icon { color: var(--color-info); flex-shrink: 0; }
    .dir-status p { margin: 0; font-size: var(--text-sm); color: var(--color-text-primary); line-height: 1.5; }
    .dir-intro { font-size: var(--text-lg); line-height: 1.7; color: var(--color-text-secondary); max-width: 760px; margin: 0 0 36px; }
    .dir-features { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; margin-bottom: 44px; }
    .dir-feature {
      padding: 26px; background: var(--color-surface);
      border: 1px solid var(--color-border); border-radius: var(--radius-lg);
      transition: transform var(--duration-normal) var(--easing-standard), box-shadow var(--duration-normal) ease;
    }
    .dir-feature:hover { transform: translateY(-4px); box-shadow: var(--shadow-md); }
    .dir-feature-icon {
      width: 52px; height: 52px; margin-bottom: 16px;
      display: inline-flex; align-items: center; justify-content: center;
      background: var(--color-primary-50); color: var(--color-primary);
      border-radius: var(--radius-md);
    }
    .dir-feature-icon mat-icon { font-size: 27px; width: 27px; height: 27px; }
    .dir-feature h3 { font-size: var(--text-lg); font-weight: 700; color: var(--color-text-primary); margin: 0 0 8px; }
    .dir-feature p { font-size: var(--text-sm); color: var(--color-text-secondary); line-height: 1.6; margin: 0; }
    .dir-cta {
      display: flex; align-items: center; justify-content: space-between; gap: 24px; flex-wrap: wrap;
      padding: clamp(24px, 4vw, 40px);
      background:
        radial-gradient(600px 240px at 90% -30%, rgba(255,210,0,0.10), transparent 60%),
        linear-gradient(160deg, #9b0c24 0%, #6a0918 100%);
      border-radius: var(--radius-lg); color: #fff;
    }
    .dir-cta-text h2 { font-family: var(--font-serif); font-size: var(--text-2xl); font-weight: 700; color: #fff; margin: 0 0 6px; }
    .dir-cta-text p { font-size: var(--text-base); color: rgba(255,255,255,0.85); margin: 0; max-width: 520px; line-height: 1.5; }
    .dir-cta-btn {
      display: inline-flex; align-items: center; gap: 8px; flex-shrink: 0;
      padding: 13px 24px; background: var(--color-secondary); color: #1a1a2e;
      border-radius: var(--radius-md); text-decoration: none; font-weight: 700; font-size: var(--text-base);
      transition: transform var(--duration-fast) var(--easing-spring), background-color var(--duration-fast) ease;
    }
    .dir-cta-btn:hover { background: var(--color-secondary-light); transform: translateY(-2px); }
    .dir-cta-btn mat-icon { font-size: 19px; width: 19px; height: 19px; }
    @media (max-width: 860px) { .dir-features { grid-template-columns: 1fr; } }
  `],
})
export class DirectoryLandingComponent {
  title = input.required<string>();
  subtitle = input<string>('');
  icon = input<string>('');
  homeLabel = input<string>('Home');
  breadcrumb = input<Crumb[]>([]);
  statusNote = input.required<string>();
  intro = input<string>('');
  features = input<DirectoryFeature[]>([]);
  ctaTitle = input.required<string>();
  ctaText = input<string>('');
  ctaLabel = input.required<string>();
  ctaRoute = input.required<string>();
  ctaIcon = input<string>('arrow_forward');
}
