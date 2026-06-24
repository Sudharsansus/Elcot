import { Injectable, signal, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class TranslationService {
  private readonly http = inject(HttpClient);
  private readonly STORAGE_KEY = 'avgc_locale';
  translations = signal<Record<string, string>>({});
  currentLocale = signal<'en' | 'ta'>((sessionStorage.getItem(this.STORAGE_KEY) as 'en' | 'ta') || 'en');
  isLoaded = signal(false);

  loadTranslations(locale: 'en' | 'ta') {
    return this.http.get<Record<string, string>>(`/assets/i18n/${locale}.json`).pipe(tap(data => { this.translations.set(data); this.isLoaded.set(true); }));
  }

  translate(key: string, params?: Record<string, string | number>): string {
    let value = this.translations()[key] || key;
    if (params) Object.entries(params).forEach(([k, v]) => { value = value.replace(`{{${k}}}`, String(v)); });
    return value;
  }

  getBilingual(en: string, ta?: string | null): { en: string; ta: string } { return { en, ta: ta || en }; }

  setLocale(locale: 'en' | 'ta'): void {
    this.currentLocale.set(locale); sessionStorage.setItem(this.STORAGE_KEY, locale); this.loadTranslations(locale).subscribe();
  }

  toggleLocale(): void { this.setLocale(this.currentLocale() === 'en' ? 'ta' : 'en'); }
}
