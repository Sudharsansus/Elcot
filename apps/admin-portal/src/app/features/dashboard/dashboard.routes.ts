export const DASHBOARD_ROUTES = [{ path: '', loadComponent: () => import('./pages/admin-dashboard.component').then(m => m.AdminDashboardComponent) }];
