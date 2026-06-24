import { Routes } from '@angular/router';

export const TALENT_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/talent-list.component').then(m => m.TalentListComponent)
  }
];
