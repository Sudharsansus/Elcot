export const USER_ROUTES = [{ path: '', loadComponent: () => import('./pages/user-management.component').then(m => m.UserManagementComponent) }];
