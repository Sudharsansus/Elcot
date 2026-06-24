import { Routes } from '@angular/router';

export const SCHEME_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/scheme-list.component').then(m => m.SchemeListComponent)
  },
  {
    path: ':id',
    loadComponent: () => import('./pages/scheme-detail.component').then(m => m.SchemeDetailComponent)
  }
];
