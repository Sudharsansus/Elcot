import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { NotificationService } from '../services/notification.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const notification = inject(NotificationService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let message = 'An unexpected error occurred';

      if (error.status === 0) {
        message = 'Network error. Please check your connection.';
      } else if (error.status >= 400 && error.status < 500) {
        message = error.error?.message || error.error?.error || getErrorMessage(error.status);
      } else if (error.status >= 500) {
        message = 'Server error. Please try again later.';
      }

      if (!req.headers.has('X-Silent-Error')) {
        notification.error(message);
      }

      return throwError(() => ({
        status: error.status,
        message,
        error: error.error
      }));
    })
  );
};

function getErrorMessage(status: number): string {
  const messages: Record<number, string> = {
    400: 'Bad request. Please check your input.',
    401: 'Session expired. Please login again.',
    403: 'You do not have permission to perform this action.',
    404: 'The requested resource was not found.',
    409: 'A conflict occurred. The resource may already exist.',
    422: 'Validation error. Please check your data.',
    429: 'Too many requests. Please try again later.'
  };
  return messages[status] || 'Request failed.';
}
