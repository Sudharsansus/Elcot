// ============================================================
// PUBLIC PORTAL — HOME PAGE (innovative + interactive, TN-Gov)
// ============================================================
// Showpiece landing page for the Tamil Nadu AVGC-XR Portal (ELCOT). Built on:
//   • a real TN-Government emblem masthead (themeable inline SVG),
//   • a perf-budgeted XR constellation canvas behind the hero,
//   • count-up stats derived from REAL policy/scheme content (no fabricated
//     operational metrics),
//   • an interactive Scheme Finder backed by the real scheme catalogue,
//   • a sector explorer, an honest application-journey explainer, and
//   • varied, reduced-motion-gated scroll reveals.
// ============================================================

import { Component, ChangeDetectionStrategy, inject, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { I18nService } from '../../../core/services/i18n.service';
import { StrapiContentService } from '../../../core/services/strapi-content.service';
import { SCHEMES_DATA, SECTOR_CATEGORIES, HOMEPAGE_NEWS, Scheme } from '../../schemes/schemes.data';
import { TnEmblemComponent } from '../../../shared/brand/tn-emblem.component';
import { HeroCanvasComponent } from '../components/hero-canvas.component';
import { SchemeFinderComponent } from '../components/scheme-finder.component';
import { RevealDirective } from '../../../shared/directives/reveal.directive';
import { CountUpDirective } from '../../../shared/directives/count-up.directive';

@Component({
  selector: 'app-home',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule, RouterModule, MatIconModule,
    TnEmblemComponent, HeroCanvasComponent, SchemeFinderComponent,
    RevealDirective, CountUpDirective,
  ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  private readonly i18n = inject(I18nService);
  private readonly strapi = inject(StrapiContentService);

  readonly lang = this.i18n.currentLanguage;
  readonly isTa = computed(() => this.lang() === 'ta');

  // Live CMS content (fails soft). Static policy content renders immediately.
  readonly cmsNews = this.strapi.news;
  readonly cmsEvents = this.strapi.events;

  readonly featuredSchemes = signal<Scheme[]>(SCHEMES_DATA.filter((s) => s.featured));
  readonly news = signal(HOMEPAGE_NEWS);
  readonly sectors = SECTOR_CATEGORIES;

  // ----------------------------------------------------------
  // TRUTHFUL hero facts — derived from the real catalogue, NOT
  // fabricated operational counters.
  // ----------------------------------------------------------
  readonly heroStats = computed(() => {
    const ta = this.isTa();
    const maxGrantCr = Math.max(...SCHEMES_DATA.map((s) => s.maxAmount)) / 10000000; // ₹1 Cr (XR infra)
    return [
      { value: SCHEMES_DATA.length, pad: 2, prefix: '', suffix: '', label: ta ? 'ஊக்கத் திட்டங்கள்' : 'Incentive Schemes' },
      { value: this.sectors.length, pad: 2, prefix: '', suffix: '', label: ta ? 'படைப்பாற்றல் துறைகள்' : 'Creative Verticals' },
      { value: 500, pad: 0, prefix: '₹', suffix: ' Cr', label: ta ? 'கொள்கை நிதி (2024–29)' : 'Policy Outlay (2024–29)' },
      { value: maxGrantCr, pad: 0, prefix: '₹', suffix: ' Cr', label: ta ? 'அதிகபட்ச மானியம் / திட்டம்' : 'Max Grant / Project' },
    ];
  });

  // Sector explorer cards (real 5 verticals + a one-line role).
  readonly sectorCards = computed(() => {
    const ta = this.isTa();
    const blurbs: Record<string, { en: string; ta: string }> = {
      ANIMATION: { en: 'Studios, feature & series production, IP creation.', ta: 'ஸ்டுடியோக்கள், தொடர் தயாரிப்பு, IP உருவாக்கம்.' },
      VFX: { en: 'Visual effects, post-production, virtual production.', ta: 'காட்சி விளைவுகள், போஸ்ட்-புரொடக்ஷன்.' },
      GAMING: { en: 'Game studios, esports, interactive media.', ta: 'விளையாட்டு ஸ்டுடியோக்கள், இ-ஸ்போர்ட்ஸ்.' },
      COMICS: { en: 'Comics, graphic novels, webtoons & publishing.', ta: 'காமிக்ஸ், கிராஃபிக் நாவல்கள், வெப்டூன்.' },
      XR: { en: 'AR / VR / MR, immersive & spatial experiences.', ta: 'AR / VR / MR, இமெர்சிவ் அனுபவங்கள்.' },
    };
    return this.sectors.map((s) => ({
      ...s,
      label: ta ? s.labelTa : s.labelEn,
      blurb: ta ? blurbs[s.key].ta : blurbs[s.key].en,
    }));
  });

  // Honest "how applications work" — describes the REAL process flow, not
  // fabricated live status data.
  readonly journey = computed(() => {
    const ta = this.isTa();
    const steps = [
      { icon: 'how_to_reg', en: 'Register', ta: 'பதிவு', dEn: 'Create your applicant profile (studio, individual or student).', dTa: 'உங்கள் விண்ணப்பதாரர் சுயவிவரத்தை உருவாக்குங்கள்.' },
      { icon: 'fact_check', en: 'Check eligibility', ta: 'தகுதி சரிபார்ப்பு', dEn: 'Use the Scheme Finder to match schemes to your profile.', dTa: 'திட்டம் கண்டறிதல் மூலம் பொருத்துங்கள்.' },
      { icon: 'upload_file', en: 'Apply & upload', ta: 'விண்ணப்பம் & பதிவேற்றம்', dEn: 'Submit the form and required documents online.', dTa: 'படிவம் மற்றும் ஆவணங்களைச் சமர்ப்பியுங்கள்.' },
      { icon: 'reviews', en: 'Review', ta: 'மதிப்பாய்வு', dEn: 'Departmental verification and evaluation of your application.', dTa: 'துறை சரிபார்ப்பு மற்றும் மதிப்பீடு.' },
      { icon: 'verified', en: 'Sanction & disbursal', ta: 'ஒப்புதல் & வழங்கல்', dEn: 'Approval and incentive disbursement to your account.', dTa: 'ஒப்புதல் மற்றும் ஊக்கத்தொகை வழங்கல்.' },
    ];
    return steps.map((s, i) => ({ ...s, index: i + 1, label: ta ? s.ta : s.en, desc: ta ? s.dTa : s.dEn }));
  });

  ngOnInit(): void {
    // Pull live News/Events from Strapi 5 (fails soft — see StrapiContentService).
    void this.strapi.fetchNews({ limit: 3 });
    void this.strapi.fetchEvents({ limit: 3 });
  }

  // ---- helpers ----
  schemeName(s: Scheme): string { return this.isTa() ? s.nameTa : s.name; }
  schemeDescription(s: Scheme): string { return this.isTa() ? s.descriptionTa : s.description; }
  categoryLabel(s: Scheme): string { return this.t(`schemes.categories.${s.category}`); }
  newsTitle(n: typeof HOMEPAGE_NEWS[number]): string { return this.isTa() ? n.titleTa : n.titleEn; }
  newsExcerpt(n: typeof HOMEPAGE_NEWS[number]): string { return this.isTa() ? n.excerptTa : n.excerptEn; }

  formatAmount(amount: number): string {
    if (amount >= 10000000) return `₹${(amount / 10000000).toFixed(amount % 10000000 === 0 ? 0 : 1)} Cr`;
    if (amount >= 100000) return `₹${(amount / 100000).toFixed(amount % 100000 === 0 ? 0 : 1)} L`;
    return `₹${amount.toLocaleString('en-IN')}`;
  }

  formatDate(dateStr: string): string {
    const d = new Date(dateStr);
    return d.toLocaleDateString(this.isTa() ? 'ta-IN' : 'en-IN', { year: 'numeric', month: 'long', day: 'numeric' });
  }

  processingTime(s: Scheme): string { return this.isTa() ? s.processingTimeTa : s.processingTime; }

  t(key: string): string { return this.i18n.translate(key); }
}
