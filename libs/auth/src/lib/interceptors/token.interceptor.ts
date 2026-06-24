import { throwError } from 'rxjs';
import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, switchMap } from 'rxjs/operators';

const AUTH_ENDPOINTS = ['/api/v1/auth/login', '/api/v1/auth/register', '/api/v1/auth/password'];

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  if (!req.url.startsWith('/api/') || AUTH_ENDPOINTS.some(e => req.url.includes(e))) return next(req);

  const token = sessionStorage.getItem('avgc_access_token');
  if (token) req = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 && !AUTH_ENDPOINTS.some(e => req.url.includes(e))) {
        return authService.refreshToken().pipe(
          switchMap(response => { const cloned = req.clone({ setHeaders: { Authorization: `Bearer ${response.accessToken}` } }); return next(cloned); }),
          catchError(refreshError => { authService.logout().subscribe(); return throwError(() => refreshError); })
        );
      }
      return throwError(() => error);
    })
  );
};

import { AuthService } from '../services/auth.service';
