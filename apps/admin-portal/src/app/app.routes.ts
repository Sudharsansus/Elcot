import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./features/dashboard/pages/admin-dashboard.component').then(m => m.AdminDashboardComponent),
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN', 'REVIEWER'] }
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./features/dashboard/pages/admin-dashboard.component').then(m => m.AdminDashboardComponent),
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN', 'REVIEWER'] }
  },
  {
    path: 'applications',
    loadChildren: () => import('./features/applications/applications.routes').then(m => m.APPLICATION_ROUTES),
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN', 'REVIEWER'] }
  },
  {
    path: 'reports',
    loadComponent: () => import('./features/reports/pages/ai-reports.component').then(m => m.AiReportsComponent),
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN', 'REVIEWER'] }
  },
  {
    path: 'workflow',
    loadChildren: () => import('./features/workflow/workflow.routes').then(m => m.WORKFLOW_ROUTES),
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] }
  },
  {
    path: 'users',
    loadChildren: () => import('./features/users/users.routes').then(m => m.USER_ROUTES),
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] }
  },
  {
    path: 'auth',
    loadChildren: () => import('./features/auth/auth.routes').then(m => m.AUTH_ROUTES)
  },
  {
    path: '**',
    redirectTo: ''
  }
];
