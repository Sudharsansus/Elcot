import { HttpInterceptorFn } from '@angular/common/http';
import { environment } from '../../../environments/environment';

function generateRequestId(): string {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
    const r = (Math.random() * 16) | 0;
    const v = c === 'x' ? r : (r & 0x3) | 0x8;
    return v.toString(16);
  });
}

export const tracingInterceptor: HttpInterceptorFn = (req, next) => {
  if (!req.url.startsWith('/api/')) {
    return next(req);
  }

  const requestId = generateRequestId();
  const cloned = req.clone({
    setHeaders: {
      'X-Request-Id': requestId
    }
  });

  if (environment.enableDevTools) {
    console.debug(`[HTTP] ${req.method} ${req.url} [requestId: ${requestId}]`);
  }

  return next(cloned);
};
