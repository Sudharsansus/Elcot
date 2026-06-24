import { throwError } from 'rxjs';
import { HttpInterceptorFn, HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let msg = 'An unexpected error occurred. Please try again.';
      switch (error.status) {
        case HttpStatusCode.BadRequest: msg = error.error?.message || 'Invalid request. Please check your input.'; break;
        case HttpStatusCode.Forbidden: msg = 'You do not have permission to perform this action.'; router.navigate(['/unauthorized']); break;
        case HttpStatusCode.NotFound: msg = 'The requested resource was not found.'; break;
        case HttpStatusCode.TooManyRequests: msg = 'Too many requests. Please wait a moment and try again.'; break;
        case HttpStatusCode.ServiceUnavailable: msg = 'Service temporarily unavailable. Please try again in a few minutes.'; break;
        default: if (error.status === 0) msg = 'Unable to connect to the server. Please check your internet connection.';
      }
      console.error(`[API Error] ${req.method} ${req.url}:`, error.status, error.message);
      return throwError(() => ({ ...error, userMessage: msg, status: error.status }));
    })
  );
};
