// ============================================================
// SCHEME FINDER — interactive eligibility wizard (real data)
// ============================================================
// "Who are you?" → live-filtered list of the schemes that genuinely apply to
// that applicant, with an animated match count. The profile→category mapping
// below is derived from each scheme's REAL eligibility text in schemes.data.ts
// (no fabricated matching). Fully keyboard-operable with aria-pressed chips and
// an aria-live result count.
// ============================================================

import {
  Component, ChangeDetectionStrategy, inject, signal, computed,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { I18nService } from '../../../core/services/i18n.service';
import { SCHEMES_DATA, Scheme } from '../../schemes/schemes.data';

type ProfileKey = 'studio' | 'startup' | 'freelancer' | 'student' | 'trainee';

interface Profile {
  key: ProfileKey;
  icon: string;
  labelEn: string;
  labelTa: string;
  /** Scheme categories this applicant is genuinely eligible for (from eligibility text). */
  categories: Scheme['category'][];
}

@Component({
  selector: 'app-scheme-finder',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, RouterModule, MatIconModule],
  templateUrl: './scheme-finder.component.html',
  styleUrls: ['./scheme-finder.component.scss'],
})
export class SchemeFinderComponent {
  private readonly i18n = inject(I18nService);
  readonly lang = this.i18n.currentLanguage;

  readonly profiles: Profile[] = [
    { key: 'studio',     icon: 'movie_filter', labelEn: 'Studio / Company', labelTa: 'ஸ்டுடியோ / நிறுவனம்', categories: ['production', 'infrastructure', 'export'] },
    { key: 'startup',    icon: 'rocket_launch', labelEn: 'Startup / New studio', labelTa: 'ஸ்டார்ட்அப்', categories: ['infrastructure', 'production', 'export'] },
    { key: 'freelancer', icon: 'badge', labelEn: 'Freelancer', labelTa: 'ஃப்ரீலான்ஸர்', categories: ['freelancer'] },
    { key: 'student',    icon: 'school', labelEn: 'Student', labelTa: 'மாணவர்', categories: ['scholarship', 'training'] },
    { key: 'trainee',    icon: 'work', labelEn: 'Job-seeker / Trainee', labelTa: 'பயிற்சியாளர்', categories: ['training'] },
  ];

  readonly selected = signal<ProfileKey | null>(null);

  readonly matched = computed<Scheme[]>(() => {
    const key = this.selected();
    if (!key) return SCHEMES_DATA;
    const profile = this.profiles.find((p) => p.key === key);
    if (!profile) return SCHEMES_DATA;
    return SCHEMES_DATA.filter((s) => profile.categories.includes(s.category));
  });

  readonly matchCount = computed(() => this.matched().length);

  select(key: ProfileKey): void {
    this.selected.update((cur) => (cur === key ? null : key));
  }

  reset(): void {
    this.selected.set(null);
  }

  isTa = computed(() => this.lang() === 'ta');

  schemeName(s: Scheme): string { return this.isTa() ? s.nameTa : s.name; }
  profileLabel(p: Profile): string { return this.isTa() ? p.labelTa : p.labelEn; }
  categoryLabel(s: Scheme): string { return this.i18n.translate(`schemes.categories.${s.category}`); }

  formatAmount(amount: number): string {
    if (amount >= 10000000) return `₹${(amount / 10000000).toFixed(amount % 10000000 === 0 ? 0 : 1)} Cr`;
    if (amount >= 100000) return `₹${(amount / 100000).toFixed(amount % 100000 === 0 ? 0 : 1)} L`;
    return `₹${amount.toLocaleString('en-IN')}`;
  }

  t(key: string): string { return this.i18n.translate(key); }
}
