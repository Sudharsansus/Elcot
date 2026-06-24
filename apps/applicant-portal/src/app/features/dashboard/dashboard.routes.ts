export const DASHBOARD_ROUTES = [{ path: '', loadComponent: () => import('./pages/applicant-dashboard.component').then(m => m.ApplicantDashboardComponent) }];
