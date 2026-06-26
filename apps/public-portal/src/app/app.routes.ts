// ============================================================
// UPDATED: app.routes.ts (add contact + grievance routes)
// ============================================================
// Replace: apps/public-portal/src/app/app.routes.ts
// ============================================================

import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./features/home/pages/home.component').then(m => m.HomeComponent)
  },
  {
    path: 'schemes',
    loadChildren: () => import('./features/schemes/schemes.routes').then(m => m.SCHEME_ROUTES)
  },
  {
    path: 'companies',
    loadChildren: () => import('./features/companies/companies.routes').then(m => m.COMPANY_ROUTES)
  },
  {
    path: 'talent',
    loadChildren: () => import('./features/talent/talent.routes').then(m => m.TALENT_ROUTES)
  },
  {
    path: 'freelancers',
    loadChildren: () => import('./features/freelancers/freelancers.routes').then(m => m.FREELANCER_ROUTES)
  },
  {
    path: 'events',
    loadChildren: () => import('./features/events/events.routes').then(m => m.EVENT_ROUTES)
  },
  {
    path: 'resources',
    loadChildren: () => import('./features/resources/resources.routes').then(m => m.RESOURCE_ROUTES)
  },
  {
    path: 'about',
    loadChildren: () => import('./features/about/about.routes').then(m => m.ABOUT_ROUTES)
  },
  {
    path: 'contact',
    loadComponent: () => import('./features/contact/pages/contact.component').then(m => m.ContactComponent)
  },
  {
    path: 'grievance',
    loadComponent: () => import('./features/contact/pages/contact.component').then(m => m.ContactComponent)
  },
  {
    path: 'auth',
    loadChildren: () => import('./features/auth/auth.routes').then(m => m.AUTH_ROUTES)
  },
  {
    path: 'privacy',
    loadComponent: () => import('./features/legal/pages/privacy.component').then(m => m.PrivacyComponent)
  },
  {
    path: '**',
    redirectTo: ''
  }
];
