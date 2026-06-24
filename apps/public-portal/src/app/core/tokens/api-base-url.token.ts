import { InjectionToken } from '@angular/core';

export const API_BASE_URL = new InjectionToken<string>('API_BASE_URL', {
  providedIn: 'root',
  factory: () => {
    if (typeof window !== 'undefined') {
      const env = (window as unknown as Record<string, Record<string, string>>).__ENVIRONMENT__;
      if (env?.apiBaseUrl) return env.apiBaseUrl;
    }
    return '/api';
  }
});
