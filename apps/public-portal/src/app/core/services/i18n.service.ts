import { Injectable, signal, inject } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { HttpClient } from '@angular/common/http';

export interface TranslationMap {
  [key: string]: string | TranslationMap;
}

@Injectable({ providedIn: 'root' })
export class I18nService {
  private readonly http = inject(HttpClient);
  private readonly document = inject(DOCUMENT) as Document;

  private translations: Map<string, TranslationMap> = new Map();
  readonly currentLanguage = signal<string>('en');

  constructor() {
    const saved = localStorage.getItem('avgcxr-lang');
    if (saved && (saved === 'en' || saved === 'ta')) {
      this.currentLanguage.set(saved);
    }
  }

  async setLanguage(lang: 'en' | 'ta'): Promise<void> {
    if (!this.translations.has(lang)) {
      try {
        const data = await this.http.get<TranslationMap>(`/assets/i18n/${lang}.json`).toPromise();
        if (data) this.translations.set(lang, data);
      } catch (err) {
        console.error(`Failed to load ${lang} translations`, err);
        return;
      }
    }
    this.currentLanguage.set(lang);
    this.document.documentElement.lang = lang;
    localStorage.setItem('avgcxr-lang', lang);
  }

  translate(key: string, params?: Record<string, string | number>): string {
    const lang = this.currentLanguage();
    const translations = this.translations.get(lang);

    if (!translations) return key;

    const value = this.getNestedValue(translations, key);
    if (typeof value !== 'string') return key;

    return this.interpolate(value, params);
  }

  instant(key: string, params?: Record<string, string | number>): string {
    return this.translate(key, params);
  }

  getBrowserLang(): string {
    const nav = this.document.defaultView?.navigator;
    if (!nav) return 'en';
    const browserLang = nav.language || (nav as Navigator & { userLanguage?: string }).userLanguage || 'en';
    return browserLang.substring(0, 2).toLowerCase();
  }

  private getNestedValue(obj: TranslationMap, path: string): string | TranslationMap | undefined {
    const keys = path.split('.');
    let current: string | TranslationMap | undefined = obj;
    for (const key of keys) {
      if (current && typeof current === 'object') {
        current = (current as TranslationMap)[key];
      } else {
        return undefined;
      }
    }
    return current;
  }

  private interpolate(template: string, params?: Record<string, string | number>): string {
    if (!params) return template;
    return template.replace(/\{(\w+)\}/g, (match, key) => {
      const value = params[key];
      return value !== undefined ? String(value) : match;
    });
  }
}
