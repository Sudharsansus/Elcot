import { Routes } from '@angular/router';
export const APPLICATION_ROUTES: Routes = [
  { path: '', loadComponent: () => import('./pages/my-applications.component').then(m => m.MyApplicationsComponent) },
  { path: 'apply/:schemeId', loadComponent: () => import('./pages/apply-scheme.component').then(m => m.ApplySchemeComponent) },
];
