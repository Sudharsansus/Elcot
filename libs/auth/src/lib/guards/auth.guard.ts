import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Role } from '@avgc-xr/api-contracts';

export function authGuard(requiredRoles?: Role[]): CanActivateFn {
  return (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);
    if (!authService.currentUser) { router.navigate(['/auth/login'], { queryParams: { returnUrl: state.url } }); return false; }
    if (requiredRoles?.length) { if (!authService.hasAnyRole(requiredRoles)) { router.navigate(['/unauthorized']); return false; } }
    return true;
  };
}

export function guestGuard(): CanActivateFn {
  return () => { const authService = inject(AuthService); const router = inject(Router); if (authService.currentUser) { router.navigate(['/dashboard']); return false; } return true; };
}
