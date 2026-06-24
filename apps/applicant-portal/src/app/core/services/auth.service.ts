import { Injectable, signal, computed, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { API_BASE_URL } from '../tokens/api-base-url.token';
import { User, AuthResponse, LoginRequest } from '@avgcxr/api-contracts';

interface JwtPayload {
  sub: string;
  email: string;
  roles: string[];
  exp: number;
  iat: number;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly baseUrl = inject(API_BASE_URL);

  readonly currentUser = signal<User | null>(null);
  readonly isAuthenticated = computed(() => !!this.currentUser());
  readonly currentRoles = computed(() => this.currentUser()?.roles ?? []);

  constructor() {
    this.loadFromStorage();
  }

  private loadFromStorage(): void {
    const userJson = localStorage.getItem('avgcxr-user');
    const token = localStorage.getItem('avgcxr-token');
    if (userJson && token) {
      try {
        const user: User = JSON.parse(userJson);
        this.currentUser.set(user);
      } catch {
        this.clearStorage();
      }
    }
  }

  private clearStorage(): void {
    localStorage.removeItem('avgcxr-token');
    localStorage.removeItem('avgcxr-refresh-token');
    localStorage.removeItem('avgcxr-user');
    this.currentUser.set(null);
  }

  getAccessToken(): string | null {
    return localStorage.getItem('avgcxr-token');
  }

  async login(email: string, password: string): Promise<void> {
    const response = await this.http.post<AuthResponse>(`${this.baseUrl}/auth/login`, { email, password } as LoginRequest).toPromise();
    if (!response) throw new Error('Login failed');
    this.handleAuthResponse(response);
  }

  async register(data: { name: string; email: string; phone: string; role: string; password: string }): Promise<void> {
    await this.http.post<void>(`${this.baseUrl}/auth/register`, data).toPromise();
  }

  async forgotPassword(email: string): Promise<void> {
    await this.http.post<void>(`${this.baseUrl}/auth/forgot-password`, { email }).toPromise();
  }

  async resetPassword(token: string, password: string): Promise<void> {
    await this.http.post<void>(`${this.baseUrl}/auth/reset-password`, { token, password }).toPromise();
  }

  async refreshToken(): Promise<void> {
    const refreshTokenValue = localStorage.getItem('avgcxr-refresh-token');
    if (!refreshTokenValue) throw new Error('No refresh token');

    const response = await this.http.post<AuthResponse>(`${this.baseUrl}/auth/refresh`, { refreshToken: refreshTokenValue }).toPromise();
    if (!response) throw new Error('Token refresh failed');
    this.handleAuthResponse(response);
  }

  logout(): void {
    this.clearStorage();
    this.router.navigate(['/auth/login']);
  }

  async loadUserProfile(): Promise<void> {
    const user = await this.http.get<User>(`${this.baseUrl}/auth/me`).toPromise();
    if (user) {
      this.currentUser.set(user);
      localStorage.setItem('avgcxr-user', JSON.stringify(user));
    }
  }

  async updateProfile(data: Partial<User>): Promise<User> {
    const user = await this.http.put<User>(`${this.baseUrl}/auth/profile`, data).toPromise();
    if (user) {
      this.currentUser.set(user);
      localStorage.setItem('avgcxr-user', JSON.stringify(user));
    }
    return user!;
  }

  hasRole(role: string): boolean {
    return (this.currentRoles() as string[]).includes(role);
  }

  hasAnyRole(roles: string[]): boolean {
    return roles.some(r => (this.currentRoles() as string[]).includes(r));
  }

  isTokenExpiringSoon(): boolean {
    const token = localStorage.getItem('avgcxr-token');
    if (!token) return true;
    try {
      const payload: JwtPayload = JSON.parse(atob(token.split('.')[1]));
      const expiresAt = payload.exp * 1000;
      const now = Date.now();
      const fiveMinutes = 5 * 60 * 1000;
      return expiresAt - now < fiveMinutes;
    } catch {
      return true;
    }
  }

  private handleAuthResponse(response: AuthResponse): void {
    localStorage.setItem('avgcxr-token', response.accessToken);
    localStorage.setItem('avgcxr-refresh-token', response.refreshToken);
    localStorage.setItem('avgcxr-user', JSON.stringify(response.user));
    this.currentUser.set(response.user);
  }
}
