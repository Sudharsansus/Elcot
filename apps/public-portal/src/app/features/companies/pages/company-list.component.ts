import { Component, ChangeDetectionStrategy, inject, computed } from '@angular/core';
import { DirectoryLandingComponent, DirectoryFeature } from '../../../shared/directory-landing/directory-landing.component';
import { Crumb } from '../../../shared/page-header/page-header.component';
import { I18nService } from '../../../core/services/i18n.service';

@Component({
  selector: 'app-company-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [DirectoryLandingComponent],
  template: `
    <app-directory-landing
      [icon]="'business_center'"
      [title]="ta() ? 'வணிக இணைப்பு' : 'Business Connect'"
      [subtitle]="ta() ? 'தமிழ்நாடு முழுவதும் உள்ள AVGC-XR நிறுவனங்கள் மற்றும் ஸ்டுடியோக்கள்' : 'AVGC-XR companies, studios and service providers across Tamil Nadu'"
      [homeLabel]="ta() ? 'முகப்பு' : 'Home'"
      [breadcrumb]="crumbs()"
      [statusNote]="ta() ? 'நிறுவனங்கள் பதிவுசெய்து ELCOT ஆல் சரிபார்க்கப்பட்டவுடன் பட்டியல்கள் இங்கே தோன்றும்.' : 'Company listings appear here as studios register and are verified by ELCOT — no placeholder entries are shown.'"
      [intro]="ta() ? 'AVGC-XR நிறுவனங்கள், ஸ்டுடியோக்கள் மற்றும் சேவை வழங்குநர்களைக் கண்டறிய தேடக்கூடிய அடைவு.' : 'A searchable directory to discover AVGC-XR companies, studios and service providers — find partners, suppliers and collaborators across the Tamil Nadu ecosystem.'"
      [features]="features()"
      [ctaTitle]="ta() ? 'உங்கள் ஸ்டுடியோவைப் பட்டியலிடுங்கள்' : 'List your studio'"
      [ctaText]="ta() ? 'உங்கள் நிறுவனத்தைப் பதிவுசெய்து தமிழ்நாட்டின் AVGC-XR அடைவில் இடம்பெறுங்கள்.' : 'Register your company to be discoverable in the Tamil Nadu AVGC-XR directory.'"
      [ctaLabel]="ta() ? 'நிறுவனத்தைப் பதிவு செய்க' : 'Register your company'"
      [ctaRoute]="'/auth/register'"
      [ctaIcon]="'add_business'">
    </app-directory-landing>
  `,
})
export class CompanyListComponent {
  private readonly i18n = inject(I18nService);
  readonly ta = computed(() => this.i18n.currentLanguage() === 'ta');
  readonly crumbs = computed<Crumb[]>(() => [{ label: this.ta() ? 'வணிக இணைப்பு' : 'Business Connect' }]);
  readonly features = computed<DirectoryFeature[]>(() => this.ta() ? [
    { icon: 'search', title: 'தேடக்கூடிய அடைவு', desc: 'துறை, இடம் மற்றும் சேவையின் அடிப்படையில் நிறுவனங்களைக் கண்டறியவும்.' },
    { icon: 'verified', title: 'சரிபார்க்கப்பட்ட ஸ்டுடியோக்கள்', desc: 'ELCOT ஆல் சரிபார்க்கப்பட்ட பதிவுசெய்யப்பட்ட நிறுவனங்கள்.' },
    { icon: 'handshake', title: 'இணைந்து செயல்படுங்கள்', desc: 'கூட்டாளர்கள், சப்ளையர்கள் மற்றும் கூட்டுத் தயாரிப்பாளர்களைக் கண்டறியவும்.' },
  ] : [
    { icon: 'search', title: 'Searchable directory', desc: 'Find companies by vertical, location and service offering.' },
    { icon: 'verified', title: 'Verified studios', desc: 'Registered organisations verified by ELCOT before listing.' },
    { icon: 'handshake', title: 'Connect & collaborate', desc: 'Discover partners, suppliers and co-production opportunities.' },
  ]);
}
