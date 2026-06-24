import { Pipe, PipeTransform, LOCALE_ID, inject } from '@angular/core';

@Pipe({ name: 'avgcRelativeTime', standalone: true, pure: true })
export class RelativeTimePipe implements PipeTransform {
  private locale = inject(LOCALE_ID);

  transform(value: string | Date, locale?: 'en' | 'ta'): string {
    if (!value) return '';
    const loc = locale || (this.locale.startsWith('ta') ? 'ta' : 'en');
    const diff = Date.now() - new Date(value).getTime();
    const mins = Math.floor(diff / 60000);
    const hrs = Math.floor(mins / 60);
    const days = Math.floor(hrs / 24);
    const months = Math.floor(days / 30);
    const t = { en: { just: 'Just now', m: n => `${n} minute${n>1?'s':''} ago`, h: n => `${n} hour${n>1?'s':''} ago`, d: n => `${n} day${n>1?'s':''} ago`, mo: n => `${n} month${n>1?'s':''} ago` },
      ta: { just: 'இப்போது', m: n => `${n} நிமிடம் முன்பு`, h: n => `${n} மணிநேரம் முன்பு`, d: n => `${n} நாள் முன்பு`, mo: n => `${n} மாதம் முன்பு` } };
    const tr = t[loc];
    if (mins < 1) return tr.just;
    if (mins < 60) return tr.m(mins);
    if (hrs < 24) return tr.h(hrs);
    if (days < 30) return tr.d(days);
    return tr.mo(months);
  }
}
