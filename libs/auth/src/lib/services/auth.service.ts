import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { tap, catchError, switchMap } from 'rxjs/operators';
import { LoginRequest, LoginResponse, RegistrationRequest, User, PasswordResetRequest, PasswordChangeRequest, Role } from '@avgc-xr/api-contracts';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly API_BASE = '/api/v1/auth';
  private currentUserSubject = new BehaviorSubject<User | null>(this.loadUserFromSession());
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(!!this.loadUserFromSession());
  private tokenExpiryTimer: ReturnType<typeof setTimeout> | null = null;

  readonly currentUser$ = this.currentUserSubject.asObservable();
  readonly isAuthenticated$ = this.isAuthenticatedSubject.asObservable();
  get currentUser(): User | null { return this.currentUserSubject.value; }

  hasRole(role: Role): boolean { return this.currentUser?.roles.includes(role) ?? false; }
  hasAnyRole(roles: Role[]): boolean { if (!this.currentUser) return false; return roles.some(r => this.currentUser!.roles.includes(r)); }

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.API_BASE}/login`, request).pipe(
      tap(response => { this.storeTokens(response); this.currentUserSubject.next(response.user); this.isAuthenticatedSubject.next(true); this.storeUserRoles(response.user.roles); this.scheduleTokenRefresh(response.expiresIn); }),
      catchError(error => { this.clearSession(); return throwError(() => error); })
    );
  }

  register(request: RegistrationRequest): Observable<User> {
    return this.http.post<User>(`${this.API_BASE}/register`, request).pipe(tap(user => { this.currentUserSubject.next(user); this.isAuthenticatedSubject.next(true); }));
  }

  logout(): Observable<void> {
    return this.http.post<void>(`${this.API_BASE}/logout`, {}).pipe(tap(() => this.clearSession()), catchError(() => { this.clearSession(); return of(undefined); }));
  }

  requestPasswordReset(request: PasswordResetRequest): Observable<void> { return this.http.post<void>(`${this.API_BASE}/password/reset-request`, request); }
  changePassword(request: PasswordChangeRequest): Observable<void> { return this.http.post<void>(`${this.API_BASE}/password/change`, request); }

  refreshToken(): Observable<LoginResponse> {
    const rt = sessionStorage.getItem('avgc_refresh_token');
    if (!rt) { this.clearSession(); return throwError(() => new Error('No refresh token')); }
    return this.http.post<LoginResponse>(`${this.API_BASE}/refresh`, { refreshToken: rt }).pipe(
      tap(response => { this.storeTokens(response); this.scheduleTokenRefresh(response.expiresIn); }),
      catchError(error => { this.clearSession(); return throwError(() => error); })
    );
  }

  verifyOtp(mobileNumber: string, otp: string): Observable<{ verified: boolean }> { return this.http.post<{ verified: boolean }>(`${this.API_BASE}/verify-otp`, { mobileNumber, otp }); }
  resendOtp(mobileNumber: string): Observable<{ sent: boolean }> { return this.http.post<{ sent: boolean }>(`${this.API_BASE}/resend-otp`, { mobileNumber }); }

  private storeTokens(r: LoginResponse): void {
    sessionStorage.setItem('avgc_access_token', r.accessToken);
    sessionStorage.setItem('avgc_refresh_token', r.refreshToken);
    sessionStorage.setItem('avgc_token_expiry', String(Date.now() + r.expiresIn * 1000));
  }
  private storeUserRoles(roles: Role[]): void { sessionStorage.setItem('avgc_user_roles', JSON.stringify(roles)); }
  private loadUserFromSession(): User | null { try { const s = sessionStorage.getItem('avgc_current_user'); return s ? JSON.parse(s) : null; } catch { return null; } }
  private clearSession(): void {
    ['avgc_access_token','avgc_refresh_token','avgc_token_expiry','avgc_current_user','avgc_user_roles'].forEach(k => sessionStorage.removeItem(k));
    this.currentUserSubject.next(null); this.isAuthenticatedSubject.next(false);
    if (this.tokenExpiryTimer) { clearTimeout(this.tokenExpiryTimer); this.tokenExpiryTimer = null; }
  }
  private scheduleTokenRefresh(expiresIn: number): void {
    if (this.tokenExpiryTimer) clearTimeout(this.tokenExpiryTimer);
    this.tokenExpiryTimer = setTimeout(() => this.refreshToken().subscribe(), Math.max((expiresIn - 60) * 1000, 5000));
  }
}
