import { InjectionToken } from '@angular/core';

export const DEFAULT_LANGUAGE = new InjectionToken<'en' | 'ta'>('AVGC_XR_DEFAULT_LANGUAGE', { providedIn: 'root', factory: () => 'en' });

export const SUPPORTED_LANGUAGES = [
  { code: 'en' as const, name: 'English', nativeName: 'English' },
  { code: 'ta' as const, name: 'Tamil', nativeName: 'தமிழ்' },
] as const;

export type SupportedLanguage = (typeof SUPPORTED_LANGUAGES)[number];
