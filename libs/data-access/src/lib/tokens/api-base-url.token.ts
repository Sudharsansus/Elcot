import { InjectionToken } from '@angular/core';

/**
 * Base URL the shared ApiClientService prefixes onto every request.
 * Defaults to the same-origin "/api/v1" (useful with a dev proxy); each app
 * overrides it with its environment.apiUrl so the deployed static sites call
 * the real backend instead of their own origin.
 */
export const API_BASE_URL = new InjectionToken<string>('DATA_ACCESS_API_BASE_URL', {
  providedIn: 'root',
  factory: () => '/api/v1',
});
