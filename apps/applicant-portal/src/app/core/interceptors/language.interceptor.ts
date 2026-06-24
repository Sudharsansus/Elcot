import { HttpInterceptorFn } from '@angular/common/http';

export const languageInterceptor: HttpInterceptorFn = (req, next) => {
  const lang = localStorage.getItem('avgcxr-lang') || 'en';

  if (req.url.startsWith('/api/')) {
    req = req.clone({
      setHeaders: {
        'Accept-Language': lang
      }
    });
  }

  return next(req);
};
