import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const correlationId = crypto.randomUUID();
  const clonedReq = req.clone({ setHeaders: { 'X-Correlation-ID': correlationId, 'X-Request-ID': correlationId } });
  return next(clonedReq);
};
