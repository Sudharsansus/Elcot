import { Routes } from '@angular/router';

export const COMPANY_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/company-list.component').then(m => m.CompanyListComponent)
  }
];
