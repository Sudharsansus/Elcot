// ============================================================
// PUBLIC PORTAL — HOME PAGE
// ============================================================
// Drop into: apps/public-portal/src/app/features/home/pages/
// Files: home.component.ts (this) + home.component.html + home.component.scss
// Replaces existing home component.
// ============================================================

import { Component, ChangeDetectionStrategy, inject, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { I18nService } from '../../../core/services/i18n.service';
import { SCHEMES_DATA, HOMEPAGE_STATS, HOMEPAGE_NEWS, Scheme } from '../../schemes/schemes.data';
import { StateMessageComponent } from '../../../shared/state-message.component';
import { StrapiContentService } from '../../../core/services/strapi-content.service';

@Component({
  selector: 'app-home',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, RouterModule, MatIconModule, MatButtonModule, StateMessageComponent],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  private readonly i18n = inject(I18nService);
  private readonly strapi = inject(StrapiContentService);

  readonly lang = this.i18n.currentLanguage;
  // Live CMS-driven content (Strapi 5), populated on init. The template still
  // renders the static `news` signal as a fallback; bind to cmsNews()/cmsEvents()
  // once the CMS is populated and the frontend build is verified.
  readonly cmsNews = this.strapi.news;
  readonly cmsEvents = this.strapi.events;
  readonly cmsError = this.strapi.error;
  readonly featuredSchemes = signal<Scheme[]>(SCHEMES_DATA.filter((s: Scheme) => s.featured));
  readonly stats = signal(HOMEPAGE_STATS);
  readonly news = signal(HOMEPAGE_NEWS);
  readonly loading = signal(false);  // Static data, no loading state
  readonly error = signal<string | null>(null);

  // Use computed for current language reactive text
  readonly statsDisplay = computed(() => {
    const lang = this.lang();
    return [
      {
        icon: 'description',
        value: this.stats().applications.toLocaleString('en-IN'),
        label: lang === 'ta' ? 'விண்ணப்பங்கள் பெறப்பட்டன' : 'Applications Received',
        color: 'var(--color-primary)'
      },
      {
        icon: 'groups',
        value: this.stats().beneficiaries.toLocaleString('en-IN'),
        label: lang === 'ta' ? 'பயனாளிகள் ஆதரிக்கப்பட்டனர்' : 'Beneficiaries Supported',
        color: 'var(--color-secondary)'
      },
      {
        icon: 'business',
        value: this.stats().studios.toLocaleString('en-IN'),
        label: lang === 'ta' ? 'பதிவு செய்யப்பட்ட ஸ்டுடியோக்கள்' : 'Registered Studios',
        color: 'var(--color-success)'
      },
      {
        icon: 'person',
        value: this.stats().freelancers.toLocaleString('en-IN'),
        label: lang === 'ta' ? 'செயலில் உள்ள ஃப்ரீலான்ஸர்கள்' : 'Active Freelancers',
        color: 'var(--color-info)'
      }
    ];
  });

  readonly ecosystemCards = computed(() => {
    const lang = this.lang();
    return [
      {
        icon: 'business_center',
        title: lang === 'ta' ? 'வணிக இணைப்பு' : 'Business Connect',
        description: lang === 'ta'
          ? 'தமிழ்நாடு முழுவதும் உள்ள AVGC-XR நிறுவனங்கள், ஸ்டுடியோக்கள் மற்றும் சேவை வழங்குநர்களின் தேடக்கூடிய அடைவு.'
          : 'A searchable directory of AVGC-XR companies, studios, service providers, and ecosystem partners across Tamil Nadu.',
        route: '/companies',
        cta: lang === 'ta' ? 'நிறுவனங்களை உலாவு' : 'Browse companies'
      },
      {
        icon: 'school',
        title: lang === 'ta' ? 'திறமை இணைப்பு' : 'Talent Connect',
        description: lang === 'ta'
          ? 'மாணவர்கள் மற்றும் நிபுணர்களுக்கான டிஜிட்டல் திறன் பதிவேடு. வேலைவாய்ப்பு, இன்டர்ன்ஷிப் மற்றும் தொழில் வாய்ப்புகளைக் கண்டறியவும்.'
          : 'A digital skill registry for students and professionals. Discover jobs, internships, and career opportunities.',
        route: '/talent',
        cta: lang === 'ta' ? 'திறமையாளர்களைக் காண்க' : 'Explore talent'
      },
      {
        icon: 'badge',
        title: lang === 'ta' ? 'ஃப்ரீலான்ஸர் பதிவேடு' : 'Freelancer Registry',
        description: lang === 'ta'
          ? 'நிறுவனங்கள் மற்றும் பங்குதாரர்களால் சரிபார்க்கப்பட்ட ஃப்ரீலான்ஸர்களின் சுயவிவர அடிப்படையிலான கண்டுபிடிப்பு.'
          : 'A dedicated registry enabling profile-based discoverability of verified freelancers by companies and stakeholders.',
        route: '/freelancers',
        cta: lang === 'ta' ? 'ஃப்ரீலான்ஸர்களைக் காண்க' : 'Find freelancers'
      }
    ];
  });

  ngOnInit(): void {
    // Static data renders immediately. Additionally pull dynamic News/Events from
    // the Strapi 5 CMS (fails soft — see StrapiContentService), so the portal is a
    // real headless-CMS consumer. Bind the template to cmsNews()/cmsEvents() once
    // the CMS has published content and the build is verified.
    void this.strapi.fetchNews({ limit: 3 });
    void this.strapi.fetchEvents({ limit: 3 });
  }

  formatAmount(amount: number): string {
    if (amount >= 10000000) {
      return `₹${(amount / 10000000).toFixed(amount % 10000000 === 0 ? 0 : 1)} Crore`;
    }
    if (amount >= 100000) {
      return `₹${(amount / 100000).toFixed(amount % 100000 === 0 ? 0 : 1)} Lakh`;
    }
    return `₹${amount.toLocaleString('en-IN')}`;
  }

  getSchemeName(s: Scheme): string {
    return this.lang() === 'ta' ? s.nameTa : s.name;
  }

  getSchemeDescription(s: Scheme): string {
    return this.lang() === 'ta' ? s.descriptionTa : s.description;
  }

  getCategoryLabel(s: Scheme): string {
    return this.t(`schemes.categories.${s.category}`);
  }

  formatDate(dateStr: string): string {
    const d = new Date(dateStr);
    return d.toLocaleDateString(this.lang() === 'ta' ? 'ta-IN' : 'en-IN', {
      year: 'numeric', month: 'long', day: 'numeric'
    });
  }

  getNewsTitle(n: typeof HOMEPAGE_NEWS[number]): string {
    return this.lang() === 'ta' ? n.titleTa : n.titleEn;
  }
  getNewsExcerpt(n: typeof HOMEPAGE_NEWS[number]): string {
    return this.lang() === 'ta' ? n.excerptTa : n.excerptEn;
  }

  t(key: string): string { return this.i18n.translate(key); }
}
