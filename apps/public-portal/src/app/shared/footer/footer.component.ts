// ============================================================
// PUBLIC PORTAL — FOOTER
// ============================================================
// Drop into: apps/public-portal/src/app/shared/footer/
// ============================================================

import { Component, ChangeDetectionStrategy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { I18nService } from '../../core/services/i18n.service';

@Component({
  selector: 'app-footer',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, RouterModule, MatIconModule],
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent {
  private readonly i18n = inject(I18nService);
  readonly lang = this.i18n.currentLanguage;

  readonly quickLinks = [
    { route: '/schemes', labelKey: 'schemes.title' },
    { route: '/companies', labelKey: 'ecosystem.businessConnect' },
    { route: '/talent', labelKey: 'ecosystem.talentConnect' },
    { route: '/freelancers', labelKey: 'ecosystem.freelancerRegistry' },
    { route: '/events', labelKey: 'nav.news' }
  ];

  readonly resources = [
    { labelKey: 'footer.policy', href: '/resources/policy' },
    { labelKey: 'footer.guidelines', href: '/resources/guidelines' },
    { labelKey: 'footer.faqs', href: '/resources/faqs' },
    { labelKey: 'footer.downloads', href: '/resources/downloads' }
  ];

  t(key: string): string { return this.i18n.translate(key); }
}
