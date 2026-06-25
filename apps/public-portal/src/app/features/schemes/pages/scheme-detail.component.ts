// ============================================================
// PUBLIC PORTAL — SCHEME DETAIL (real data from SCHEMES_DATA)
// ============================================================
// Looks the scheme up by slug (route :id, bound via withComponentInputBinding)
// and renders its REAL policy content — overview, highlights, eligibility,
// required documents, benefits and key facts. Fabricated operational counters
// (applicantsCount/sanctionedCount) are intentionally NOT shown.
// ============================================================

import { Component, ChangeDetectionStrategy, inject, input, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { I18nService } from '../../../core/services/i18n.service';
import { SCHEMES_DATA, Scheme } from '../schemes.data';
import { PageHeaderComponent, Crumb } from '../../../shared/page-header/page-header.component';
import { RevealDirective } from '../../../shared/directives/reveal.directive';

@Component({
  selector: 'app-scheme-detail',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, RouterModule, MatIconModule, PageHeaderComponent, RevealDirective],
  templateUrl: './scheme-detail.component.html',
  styleUrls: ['./scheme-detail.component.scss'],
})
export class SchemeDetailComponent {
  private readonly i18n = inject(I18nService);
  readonly lang = this.i18n.currentLanguage;
  readonly isTa = computed(() => this.lang() === 'ta');

  /** Bound from the route `:id` param (carries the scheme slug). */
  readonly id = input<string>('');

  readonly scheme = computed<Scheme | undefined>(() =>
    SCHEMES_DATA.find((s) => s.slug === this.id() || s.id === this.id()),
  );

  readonly crumbs = computed<Crumb[]>(() => {
    const s = this.scheme();
    const schemesLabel = this.t('schemes.title');
    return s
      ? [{ label: schemesLabel, route: '/schemes' }, { label: this.name(s) }]
      : [{ label: schemesLabel, route: '/schemes' }];
  });

  // ---- bilingual field helpers ----
  name(s: Scheme): string { return this.isTa() ? s.nameTa : s.name; }
  description(s: Scheme): string { return this.isTa() ? s.descriptionTa : s.description; }
  department(s: Scheme): string { return this.isTa() ? s.departmentTa : s.department; }
  eligibility(s: Scheme): string { return this.isTa() ? s.eligibilityTa : s.eligibility; }
  documents(s: Scheme): string[] { return this.isTa() ? s.documentsTa : s.documents; }
  highlights(s: Scheme): string[] { return this.isTa() ? s.highlightsTa : s.highlights; }
  benefits(s: Scheme): string[] { return this.isTa() ? s.benefitsTa : s.benefits; }
  processingTime(s: Scheme): string { return this.isTa() ? s.processingTimeTa : s.processingTime; }
  categoryLabel(s: Scheme): string { return this.t(`schemes.categories.${s.category}`); }

  statusLabel(status: Scheme['status']): string {
    const map: Record<Scheme['status'], { en: string; ta: string }> = {
      active: { en: 'Active', ta: 'செயலில்' },
      upcoming: { en: 'Upcoming', ta: 'வரவிருக்கும்' },
      closed: { en: 'Closed', ta: 'மூடப்பட்டது' },
    };
    return this.isTa() ? map[status].ta : map[status].en;
  }

  formatAmount(amount: number): string {
    if (amount >= 10000000) return `₹${(amount / 10000000).toFixed(amount % 10000000 === 0 ? 0 : 1)} Crore`;
    if (amount >= 100000) return `₹${(amount / 100000).toFixed(amount % 100000 === 0 ? 0 : 1)} Lakh`;
    return `₹${amount.toLocaleString('en-IN')}`;
  }

  formatDate(dateStr: string): string {
    return new Date(dateStr).toLocaleDateString(this.isTa() ? 'ta-IN' : 'en-IN', {
      year: 'numeric', month: 'long', day: 'numeric',
    });
  }

  t(key: string): string { return this.i18n.translate(key); }
}
