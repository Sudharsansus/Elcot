import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route, _state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const user = authService.currentUser();
  if (!user) {
    router.navigate(['/auth/login']);
    return false;
  }

  const requiredRoles: string[] = route.data['roles'] || [];
  if (requiredRoles.length === 0) return true;

  const hasRole = requiredRoles.some(role => (user.roles as string[] | undefined)?.includes(role));
  if (hasRole) return true;

  router.navigate(['/unauthorized']);
  return false;
};
