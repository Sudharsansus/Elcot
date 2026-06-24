import { InjectionToken } from '@angular/core';

export interface AppConfig {
  apiBaseUrl: string;
  cmsUrl: string;
  environment: string;
  production: boolean;
  showDebug: boolean;
  version: string;
  portalName: string;
}

export const APP_CONFIG = new InjectionToken<AppConfig>('APP_CONFIG', {
  providedIn: 'root',
  factory: () => {
    if (typeof window !== 'undefined') {
      const env = (window as unknown as Record<string, AppConfig>).__ENVIRONMENT__;
      if (env) return env;
    }
    return {
      apiBaseUrl: '/api',
      cmsUrl: '/cms',
      environment: 'development',
      production: false,
      showDebug: true,
      version: '1.0.0',
      portalName: 'AVGC-XR Portal'
    };
  }
});
