import { ApplicationConfig } from '@angular/core';
import { provideRouter, withComponentInputBinding, withViewTransitions } from '@angular/router';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { TranslateModule } from '@ngx-translate/core';
import { routes } from './app.routes';
import { authInterceptor } from './core/interceptors/auth.interceptor';
import { errorInterceptor } from './core/interceptors/error.interceptor';
import { languageInterceptor } from './core/interceptors/language.interceptor';
import { tracingInterceptor } from './core/interceptors/tracing.interceptor';
import { unwrapInterceptor } from './core/interceptors/unwrap.interceptor';
import { API_BASE_URL } from './core/tokens/api-base-url.token';
import { environment } from '../environments/environment';

export const appConfig: ApplicationConfig = {
  providers: [
    // Point the API client at the real backend (the token otherwise defaults
    // to the same-origin "/api", which has no backend on the static host).
    { provide: API_BASE_URL, useValue: environment.apiUrl },
    provideRouter(routes, withComponentInputBinding(), withViewTransitions()),
    provideAnimationsAsync(),
    provideHttpClient(
      withInterceptors([tracingInterceptor, authInterceptor, languageInterceptor, unwrapInterceptor, errorInterceptor])
    ),
    TranslateModule.forRoot({
      defaultLanguage: 'en',
      useDefaultLang: true,
    }).providers ?? [],
  ]
};
