// ============================================================
// PUBLIC PORTAL — ABOUT PAGE
// ============================================================
// Drop into: apps/public-portal/src/app/features/about/pages/
// Replaces the existing about component.
// ============================================================

import { Component, ChangeDetectionStrategy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { I18nService } from '../../../core/services/i18n.service';

@Component({
  selector: 'app-about',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, RouterModule, MatIconModule, MatButtonModule],
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.scss']
})
export class AboutComponent {
  private readonly i18n = inject(I18nService);
  readonly lang = this.i18n.currentLanguage;

  // Truthful, content-derived facts (NOT fabricated operational counters).
  readonly stats = [
    { icon: 'workspace_premium', value: '6', labelEn: 'Incentive Schemes', labelTa: 'ஊக்கத் திட்டங்கள்', color: 'var(--color-primary)' },
    { icon: 'category', value: '5', labelEn: 'Creative Verticals', labelTa: 'படைப்பாற்றல் துறைகள்', color: 'var(--color-secondary)' },
    { icon: 'savings', value: '₹500 Cr', labelEn: 'Policy Outlay (2024–29)', labelTa: 'கொள்கை நிதி', color: 'var(--color-success)' },
    { icon: 'payments', value: 'Up to ₹1 Cr', labelEn: 'Per Project', labelTa: 'ஒரு திட்டத்திற்கு', color: 'var(--color-info)' }
  ];

  readonly sectors = [
    { icon: 'movie', nameEn: 'Animation', nameTa: 'அனிமேஷன்', color: 'var(--color-sector-animation)' },
    { icon: 'auto_fix_high', nameEn: 'VFX', nameTa: 'காட்சி விளைவுகள்', color: 'var(--color-sector-vfx)' },
    { icon: 'sports_esports', nameEn: 'Gaming', nameTa: 'கேமிங்', color: 'var(--color-sector-gaming)' },
    { icon: 'auto_stories', nameEn: 'Comics', nameTa: 'காமிக்ஸ்', color: 'var(--color-sector-comics)' },
    { icon: 'view_in_ar', nameEn: 'Extended Reality', nameTa: 'நீட்டிக்கப்பட்ட யதார்த்தம்', color: 'var(--color-sector-xr)' }
  ];

  t(key: string): string { return this.i18n.translate(key); }

  getSectorName(s: typeof this.sectors[number]): string {
    return this.lang() === 'ta' ? s.nameTa : s.nameEn;
  }
  getStatLabel(s: typeof this.stats[number]): string {
    return this.lang() === 'ta' ? s.labelTa : s.labelEn;
  }
}
