import { Component, ChangeDetectionStrategy, inject, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { I18nService } from '../../../core/services/i18n.service';
import { PageHeaderComponent, Crumb } from '../../../shared/page-header/page-header.component';
import { RevealDirective } from '../../../shared/directives/reveal.directive';

interface ResourceCard { icon: string; title: string; desc: string; href: string; external: boolean; }

@Component({
  selector: 'app-resource-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, RouterModule, MatIconModule, PageHeaderComponent, RevealDirective],
  template: `
    <app-page-header
      [icon]="'folder_open'"
      [title]="ta() ? 'வளங்கள்' : 'Resources'"
      [subtitle]="ta() ? 'கொள்கை ஆவணங்கள், வழிகாட்டுதல்கள் மற்றும் வணிக தொடக்க கருவித்தொகுப்பு' : 'Policy documents, guidelines and the Business Starter Toolkit'"
      [homeLabel]="ta() ? 'முகப்பு' : 'Home'"
      [breadcrumb]="crumbs()">
    </app-page-header>

    <section class="res">
      <div class="container">
        <div class="res-status" role="status" appReveal="fade">
          <mat-icon aria-hidden="true">info</mat-icon>
          <p>{{ ta()
            ? 'அதிகாரப்பூர்வ ஆவணங்கள் வெளியிடப்படும்போது இங்கே சேர்க்கப்படும். தற்போதைய சுற்றறிக்கைகளுக்கு ELCOT அதிகாரப்பூர்வ இணையதளத்தைப் பார்க்கவும்.'
            : 'Official documents are added here as they are published. For current circulars, see the ELCOT official website.' }}</p>
        </div>

        <div class="res-grid">
          @for (r of cards(); track r.title; let i = $index) {
            @if (r.external) {
              <a class="res-card" appReveal="up" [revealDelay]="i * 60" [href]="r.href" target="_blank" rel="noopener">
                <ng-container [ngTemplateOutlet]="card" [ngTemplateOutletContext]="{ $implicit: r }"></ng-container>
              </a>
            } @else {
              <a class="res-card" appReveal="up" [revealDelay]="i * 60" [routerLink]="r.href">
                <ng-container [ngTemplateOutlet]="card" [ngTemplateOutletContext]="{ $implicit: r }"></ng-container>
              </a>
            }
          }
        </div>

        <ng-template #card let-r>
          <div class="res-icon"><mat-icon aria-hidden="true">{{ r.icon }}</mat-icon></div>
          <div class="res-text">
            <h3>{{ r.title }}</h3>
            <p>{{ r.desc }}</p>
          </div>
          <mat-icon class="res-go" aria-hidden="true">{{ r.external ? 'open_in_new' : 'arrow_forward' }}</mat-icon>
        </ng-template>
      </div>
    </section>
  `,
  styles: [`
    :host { display: block; }
    .res { padding: clamp(28px, 5vw, 56px) 0 clamp(48px, 7vw, 80px); }
    .res-status {
      display: flex; align-items: center; gap: 12px; padding: 14px 18px; margin-bottom: 28px;
      background: var(--color-info-bg); border-left: 4px solid var(--color-info); border-radius: var(--radius-md);
    }
    .res-status mat-icon { color: var(--color-info); flex-shrink: 0; }
    .res-status p { margin: 0; font-size: var(--text-sm); color: var(--color-text-primary); line-height: 1.5; }
    .res-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 18px; }
    .res-card {
      display: flex; align-items: center; gap: 16px; padding: 22px;
      background: var(--color-surface); border: 1px solid var(--color-border);
      border-left: 3px solid var(--color-secondary); border-radius: var(--radius-md);
      text-decoration: none; color: var(--color-text-primary);
      transition: transform var(--duration-fast) ease, box-shadow var(--duration-fast) ease, border-color var(--duration-fast) ease;
    }
    .res-card:hover { transform: translateY(-3px); box-shadow: var(--shadow-md); border-left-color: var(--color-primary); }
    .res-icon {
      flex-shrink: 0; width: 48px; height: 48px;
      display: inline-flex; align-items: center; justify-content: center;
      background: var(--color-primary-50); color: var(--color-primary); border-radius: var(--radius-md);
    }
    .res-icon mat-icon { font-size: 26px; width: 26px; height: 26px; }
    .res-text { flex: 1; min-width: 0; }
    .res-text h3 { font-size: var(--text-base); font-weight: 700; margin: 0 0 3px; }
    .res-text p { font-size: var(--text-sm); color: var(--color-text-secondary); margin: 0; line-height: 1.5; }
    .res-go { color: var(--color-text-tertiary); flex-shrink: 0; font-size: 20px; width: 20px; height: 20px; }
    @media (max-width: 700px) { .res-grid { grid-template-columns: 1fr; } }
  `],
})
export class ResourceListComponent {
  private readonly i18n = inject(I18nService);
  readonly ta = computed(() => this.i18n.currentLanguage() === 'ta');
  readonly crumbs = computed<Crumb[]>(() => [{ label: this.ta() ? 'வளங்கள்' : 'Resources' }]);

  readonly cards = computed<ResourceCard[]>(() => this.ta() ? [
    { icon: 'policy', title: 'TN AVGC-XR கொள்கை', desc: 'தமிழ்நாடு AVGC-XR கொள்கை ஆவணம் மற்றும் சுற்றறிக்கைகள்.', href: 'https://elcot.tn.gov.in', external: true },
    { icon: 'description', title: 'விண்ணப்ப வழிகாட்டுதல்கள்', desc: 'திட்டங்களுக்கு விண்ணப்பிப்பதற்கான படிப்படியான வழிகாட்டுதல்.', href: '/schemes', external: false },
    { icon: 'quiz', title: 'அடிக்கடி கேட்கப்படும் கேள்விகள்', desc: 'திட்டங்கள் மற்றும் செயல்முறை குறித்த பொதுவான கேள்விகள்.', href: '/contact', external: false },
    { icon: 'account_balance', title: 'தமிழ்நாடு அரசு', desc: 'tn.gov.in — அதிகாரப்பூர்வ மாநில அரசு இணையதளம்.', href: 'https://tn.gov.in', external: true },
  ] : [
    { icon: 'policy', title: 'TN AVGC-XR Policy', desc: 'The Tamil Nadu AVGC-XR policy document and circulars.', href: 'https://elcot.tn.gov.in', external: true },
    { icon: 'description', title: 'Application guidelines', desc: 'Step-by-step guidance for applying to schemes.', href: '/schemes', external: false },
    { icon: 'quiz', title: 'Frequently asked questions', desc: 'Common questions on schemes and the application process.', href: '/contact', external: false },
    { icon: 'account_balance', title: 'Government of Tamil Nadu', desc: 'tn.gov.in — the official State Government website.', href: 'https://tn.gov.in', external: true },
  ]);
}
