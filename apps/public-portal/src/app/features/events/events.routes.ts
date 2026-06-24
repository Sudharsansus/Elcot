import { Routes } from '@angular/router';

export const EVENT_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/event-list.component').then(m => m.EventListComponent)
  }
];
