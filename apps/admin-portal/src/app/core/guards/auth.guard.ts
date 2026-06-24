import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (_route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    return true;
  }

  // Try token refresh if refresh token exists
  const refreshToken = localStorage.getItem('avgcxr-refresh-token');
  if (refreshToken) {
    return authService.refreshToken().then(() => true).catch(() => {
      router.navigate(['/auth/login'], { queryParams: { returnUrl: state.url } });
      return false;
    });
  }

  router.navigate(['/auth/login'], { queryParams: { returnUrl: state.url } });
  return false;
};
