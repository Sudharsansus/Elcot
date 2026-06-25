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

  readonly stats = [
    { icon: 'description', value: '7,800+', labelEn: 'Applications', labelTa: 'விண்ணப்பங்கள்', color: 'var(--color-primary)' },
    { icon: 'groups', value: '12,400+', labelEn: 'Beneficiaries', labelTa: 'பயனாளிகள்', color: 'var(--color-secondary)' },
    { icon: 'payments', value: '₹24.5 Cr', labelEn: 'Total Disbursed', labelTa: 'மொத்த வழங்கல்', color: 'var(--color-success)' },
    { icon: 'location_on', value: '32', labelEn: 'Districts Active', labelTa: 'செயலில் மாவட்டங்கள்', color: 'var(--color-info)' }
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
