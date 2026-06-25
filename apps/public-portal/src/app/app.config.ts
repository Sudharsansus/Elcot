import { ApplicationConfig, ErrorHandler, inject, provideAppInitializer } from '@angular/core';
import { provideRouter, withComponentInputBinding, withViewTransitions, withInMemoryScrolling } from '@angular/router';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient, withInterceptors, withFetch } from '@angular/common/http';
import { TranslateModule } from '@ngx-translate/core';
import { routes } from './app.routes';
import { authInterceptor } from './core/interceptors/auth.interceptor';
import { errorInterceptor } from './core/interceptors/error.interceptor';
import { languageInterceptor } from './core/interceptors/language.interceptor';
import { tracingInterceptor } from './core/interceptors/tracing.interceptor';
import { I18nService } from './core/services/i18n.service';
import { GlobalErrorHandler } from './core/global-error-handler';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(
      routes,
      withComponentInputBinding(),
      withViewTransitions(),
      withInMemoryScrolling({ scrollPositionRestoration: 'enabled', anchorScrolling: 'enabled' }),
    ),
    // SSR hydration + event replay: reuse the server-rendered DOM instead of
    // re-rendering, and replay clicks/taps captured before hydration. With a
    // CDN in front, server-render once and serve cached HTML to millions.
    provideClientHydration(withEventReplay()),
    provideAnimationsAsync(),
    provideHttpClient(
      withFetch(),
      withInterceptors([tracingInterceptor, authInterceptor, languageInterceptor, errorInterceptor])
    ),
    { provide: ErrorHandler, useClass: GlobalErrorHandler },
    TranslateModule.forRoot({
      defaultLanguage: 'en',
      useDefaultLang: true,
    }).providers ?? [],
    // Preload the active translation bundle BEFORE the first render so the UI
    // never flashes raw i18n keys (the service reads the saved/default lang in
    // its constructor; here we load that bundle). Fails soft on the server.
    provideAppInitializer(() => {
      const i18n = inject(I18nService);
      const lang = (i18n.currentLanguage() === 'ta' ? 'ta' : 'en');
      return i18n.setLanguage(lang);
    }),
  ]
};
