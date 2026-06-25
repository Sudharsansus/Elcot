// ============================================================
// PUBLIC PORTAL — NAVBAR
// ============================================================
// Drop into: apps/public-portal/src/app/shared/navbar/
// Files: navbar.component.ts (this) + navbar.component.html + navbar.component.scss
// ============================================================

import { Component, ChangeDetectionStrategy, inject, signal, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, RouterLink, RouterLinkActive } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { I18nService } from '../../core/services/i18n.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, RouterModule, RouterLink, RouterLinkActive, MatIconModule, MatButtonModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  private readonly i18n = inject(I18nService);

  readonly mobileMenuOpen = signal(false);
  readonly scrolled = signal(false);
  readonly lang = this.i18n.currentLanguage;

  readonly navLinks = [
    { route: '/', labelKey: 'nav.home', exact: true },
    { route: '/schemes', labelKey: 'nav.schemes' },
    { route: '/companies', labelKey: 'nav.businessConnect' },
    { route: '/talent', labelKey: 'nav.talentConnect' },
    { route: '/freelancers', labelKey: 'nav.freelancers' },
    { route: '/events', labelKey: 'nav.news' },
    { route: '/about', labelKey: 'nav.about' },
    { route: '/contact', labelKey: 'nav.contact' }
  ];

  @HostListener('window:scroll')
  onWindowScroll(): void {
    this.scrolled.set(window.scrollY > 20);
  }

  toggleMobileMenu(): void {
    this.mobileMenuOpen.update(v => !v);
    document.body.style.overflow = this.mobileMenuOpen() ? 'hidden' : '';
  }

  closeMobileMenu(): void {
    this.mobileMenuOpen.set(false);
    document.body.style.overflow = '';
  }

  async toggleLanguage(): Promise<void> {
    const next = this.lang() === 'en' ? 'ta' : 'en';
    await this.i18n.setLanguage(next as 'en' | 'ta');
  }

  t(key: string): string {
    return this.i18n.translate(key);
  }
}
