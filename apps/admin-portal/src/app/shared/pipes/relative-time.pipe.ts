import { Pipe, PipeTransform, inject } from '@angular/core';
import { I18nService } from '../../core/services/i18n.service';

@Pipe({
  name: 'relativeTime',
  standalone: true
})
export class RelativeTimePipe implements PipeTransform {
  private readonly i18n = inject(I18nService);

  transform(value: Date | string | number): string {
    if (!value) return '';
    const date = new Date(value);
    const now = new Date();
    const diffMs = now.getTime() - date.getTime();
    const diffSeconds = Math.floor(diffMs / 1000);
    const diffMinutes = Math.floor(diffSeconds / 60);
    const diffHours = Math.floor(diffMinutes / 60);
    const diffDays = Math.floor(diffHours / 24);
    const diffMonths = Math.floor(diffDays / 30);
    const diffYears = Math.floor(diffDays / 365);

    const lang = this.i18n.currentLanguage();

    if (lang === 'ta') {
      if (diffSeconds < 60) return 'இப்போது';
      if (diffMinutes === 1) return '1 நிமிடம் முன்';
      if (diffMinutes < 60) return `${diffMinutes} நிமிடங்கள் முன்`;
      if (diffHours === 1) return '1 மணி நேரம் முன்';
      if (diffHours < 24) return `${diffHours} மணி நேரம் முன்`;
      if (diffDays === 1) return 'நேற்று';
      if (diffDays < 30) return `${diffDays} நாட்கள் முன்`;
      if (diffMonths === 1) return '1 மாதம் முன்';
      if (diffMonths < 12) return `${diffMonths} மாதங்கள் முன்`;
      return `${diffYears} ஆண்டுகள் முன்`;
    } else {
      if (diffSeconds < 60) return 'just now';
      if (diffMinutes === 1) return '1 minute ago';
      if (diffMinutes < 60) return `${diffMinutes} minutes ago`;
      if (diffHours === 1) return '1 hour ago';
      if (diffHours < 24) return `${diffHours} hours ago`;
      if (diffDays === 1) return 'yesterday';
      if (diffDays < 30) return `${diffDays} days ago`;
      if (diffMonths === 1) return '1 month ago';
      if (diffMonths < 12) return `${diffMonths} months ago`;
      return `${diffYears} years ago`;
    }
  }
}
