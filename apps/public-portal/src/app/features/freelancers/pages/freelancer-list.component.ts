import { Component, ChangeDetectionStrategy, inject, computed } from '@angular/core';
import { DirectoryLandingComponent, DirectoryFeature } from '../../../shared/directory-landing/directory-landing.component';
import { Crumb } from '../../../shared/page-header/page-header.component';
import { I18nService } from '../../../core/services/i18n.service';

@Component({
  selector: 'app-freelancer-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [DirectoryLandingComponent],
  template: `
    <app-directory-landing
      [icon]="'badge'"
      [title]="ta() ? 'ஃப்ரீலான்ஸர் பதிவேடு' : 'Freelancer Registry'"
      [subtitle]="ta() ? 'சரிபார்க்கப்பட்ட AVGC-XR ஃப்ரீலான்ஸர்களின் அடைவு' : 'A directory of verified AVGC-XR freelancers'"
      [homeLabel]="ta() ? 'முகப்பு' : 'Home'"
      [breadcrumb]="crumbs()"
      [statusNote]="ta() ? 'ஃப்ரீலான்ஸர்கள் பதிவுசெய்து சரிபார்க்கப்பட்டவுடன் சுயவிவரங்கள் இங்கே தோன்றும்.' : 'Freelancer profiles appear here as individuals register and are verified — no placeholder profiles are shown.'"
      [intro]="ta() ? 'நிறுவனங்கள் மற்றும் பங்குதாரர்களால் சரிபார்க்கப்பட்ட ஃப்ரீலான்ஸர்களைக் கண்டறிய ஒரு பதிவேடு.' : 'A dedicated registry enabling profile-based discoverability of verified freelancers by companies and stakeholders across the state.'"
      [features]="features()"
      [ctaTitle]="ta() ? 'ஃப்ரீலான்ஸராகப் பதிவு செய்யுங்கள்' : 'Register as a freelancer'"
      [ctaText]="ta() ? 'இலவசமாகப் பதிவுசெய்து திட்ட வாய்ப்புகளுக்கு தெரிவுநிலை பெறுங்கள்.' : 'Register for free to be discoverable for project opportunities by 200+ companies.'"
      [ctaLabel]="ta() ? 'பதிவு செய்க' : 'Register now'"
      [ctaRoute]="'/auth/register'"
      [ctaIcon]="'how_to_reg'">
    </app-directory-landing>
  `,
})
export class FreelancerListComponent {
  private readonly i18n = inject(I18nService);
  readonly ta = computed(() => this.i18n.currentLanguage() === 'ta');
  readonly crumbs = computed<Crumb[]>(() => [{ label: this.ta() ? 'ஃப்ரீலான்ஸர் பதிவேடு' : 'Freelancer Registry' }]);
  readonly features = computed<DirectoryFeature[]>(() => this.ta() ? [
    { icon: 'verified_user', title: 'சரிபார்க்கப்பட்ட பேட்ஜ்', desc: 'நம்பகத்தன்மைக்கான சரிபார்க்கப்பட்ட ஃப்ரீலான்ஸர் அடையாளம்.' },
    { icon: 'photo_library', title: 'போர்ட்ஃபோலியோ', desc: 'உங்கள் சிறந்த படைப்புகளை நிறுவனங்களுக்குக் காட்டுங்கள்.' },
    { icon: 'notifications_active', title: 'திட்ட அறிவிப்புகள்', desc: 'உங்கள் திறன்களுக்குப் பொருந்தும் வேலைகளைப் பெறுங்கள்.' },
  ] : [
    { icon: 'verified_user', title: 'Verified badge', desc: 'A verified freelancer identity that builds client trust.' },
    { icon: 'photo_library', title: 'Portfolio', desc: 'Showcase your best work to studios and companies.' },
    { icon: 'notifications_active', title: 'Project alerts', desc: 'Get notified of postings matched to your skills.' },
  ]);
}
