import { Component, ChangeDetectionStrategy, inject, computed } from '@angular/core';
import { DirectoryLandingComponent, DirectoryFeature } from '../../../shared/directory-landing/directory-landing.component';
import { Crumb } from '../../../shared/page-header/page-header.component';
import { I18nService } from '../../../core/services/i18n.service';

@Component({
  selector: 'app-talent-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [DirectoryLandingComponent],
  template: `
    <app-directory-landing
      [icon]="'school'"
      [title]="ta() ? 'திறமை இணைப்பு' : 'Talent Connect'"
      [subtitle]="ta() ? 'அனிமேஷன், VFX, கேமிங், காமிக்ஸ் மற்றும் XR திறன் பதிவேடு' : 'A skill registry across animation, VFX, gaming, comics and XR'"
      [homeLabel]="ta() ? 'முகப்பு' : 'Home'"
      [breadcrumb]="crumbs()"
      [statusNote]="ta() ? 'நிபுணர்கள் மற்றும் மாணவர்கள் பதிவுசெய்தவுடன் சுயவிவரங்கள் இங்கே தோன்றும்.' : 'Talent profiles appear here as students and professionals register — no placeholder profiles are shown.'"
      [intro]="ta() ? 'மாணவர்கள் மற்றும் நிபுணர்களுக்கான டிஜிட்டல் திறன் பதிவேடு — வேலைவாய்ப்பு, இன்டர்ன்ஷிப் மற்றும் தொழில் வாய்ப்புகளை இணைக்கிறது.' : 'A digital skill registry for students and professionals — connecting talent with jobs, internships and career opportunities in the AVGC-XR sector.'"
      [features]="features()"
      [ctaTitle]="ta() ? 'உங்கள் திறன் சுயவிவரத்தை உருவாக்குங்கள்' : 'Create your talent profile'"
      [ctaText]="ta() ? 'பதிவுசெய்து உங்கள் திறன்களை நிறுவனங்களுக்குக் காட்டுங்கள்.' : 'Register to showcase your skills to studios hiring across Tamil Nadu.'"
      [ctaLabel]="ta() ? 'சுயவிவரத்தை உருவாக்கு' : 'Create profile'"
      [ctaRoute]="'/auth/register'"
      [ctaIcon]="'person_add'">
    </app-directory-landing>
  `,
})
export class TalentListComponent {
  private readonly i18n = inject(I18nService);
  readonly ta = computed(() => this.i18n.currentLanguage() === 'ta');
  readonly crumbs = computed<Crumb[]>(() => [{ label: this.ta() ? 'திறமை இணைப்பு' : 'Talent Connect' }]);
  readonly features = computed<DirectoryFeature[]>(() => this.ta() ? [
    { icon: 'badge', title: 'திறன் சுயவிவரம்', desc: 'உங்கள் திறன்கள், போர்ட்ஃபோலியோ மற்றும் அனுபவத்தைக் காட்டுங்கள்.' },
    { icon: 'work', title: 'வேலை & இன்டர்ன்ஷிப்', desc: 'நிறுவனங்களின் வாய்ப்புகளுடன் இணைக்கப்படுங்கள்.' },
    { icon: 'school', title: 'திறன் மேம்பாடு', desc: 'பயிற்சித் திட்டங்கள் மற்றும் சான்றிதழ்களை அணுகவும்.' },
  ] : [
    { icon: 'badge', title: 'Skill profile', desc: 'Showcase your skills, portfolio and experience to employers.' },
    { icon: 'work', title: 'Jobs & internships', desc: 'Get matched with opportunities from studios and companies.' },
    { icon: 'school', title: 'Upskilling', desc: 'Access training programs and recognised certifications.' },
  ]);
}
