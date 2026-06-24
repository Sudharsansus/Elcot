import { Routes } from '@angular/router';
import { publicOnlyGuard } from '../../core/guards/public-only.guard';

export const AUTH_ROUTES: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./pages/login.component').then(m => m.LoginComponent),
    canActivate: [publicOnlyGuard]
  },
  {
    path: 'register',
    loadComponent: () => import('./pages/register.component').then(m => m.RegisterComponent),
    canActivate: [publicOnlyGuard]
  },
  {
    path: 'forgot-password',
    loadComponent: () => import('./pages/forgot-password.component').then(m => m.ForgotPasswordComponent),
    canActivate: [publicOnlyGuard]
  }
];
