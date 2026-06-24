import { Routes } from '@angular/router';

export const FREELANCER_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/freelancer-list.component').then(m => m.FreelancerListComponent)
  }
];
