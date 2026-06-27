import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../../core/services/auth.service';
import { NotificationService } from '../../../core/services/notification.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  template: `
    <div class="auth-container">
      <div class="auth-card">
        <h2>Login</h2>
        <form [formGroup]="form" (ngSubmit)="onSubmit()">
          <mat-form-field appearance="outline">
            <mat-label>Email or mobile number</mat-label>
            <input matInput formControlName="email" type="text" inputmode="email" autocomplete="username" />
            @if (form.get('email')?.hasError('required')) {
              <mat-error>Email or mobile is required</mat-error>
            }
            @if (form.get('email')?.touched && form.get('email')?.hasError('pattern')) {
              <mat-error>Enter a valid email or 10-digit mobile</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Password</mat-label>
            <input matInput formControlName="password" type="password" autocomplete="current-password" />
            @if (form.get('password')?.hasError('required')) {
              <mat-error>Password is required</mat-error>
            }
          </mat-form-field>
          <button mat-raised-button color="primary" type="submit" [disabled]="loading()" class="full-width">
            {{ loading() ? 'Signing in...' : 'Sign In' }}
          </button>
        </form>
        <div class="auth-links">
          <a routerLink="/auth/forgot-password">Forgot Password?</a>
          <a routerLink="/auth/register">Create Account</a>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .auth-container {
      min-height: 100vh; display: flex; align-items: center; justify-content: center;
      padding: 40px 20px;
      background:
        radial-gradient(620px 320px at 85% -8%, rgba(124,58,237,0.5), transparent 60%),
        linear-gradient(155deg, #45125c 0%, #2c1153 50%, #181233 100%);
    }
    .auth-card {
      position: relative; background: #fff; padding: 44px 36px 32px;
      border-radius: 20px; box-shadow: 0 30px 80px -30px rgba(0,0,0,0.55);
      width: 100%; max-width: 460px;
    }
    .auth-card::before {
      content: ''; display: block; width: 64px; height: 64px; margin: 0 auto 16px;
      border-radius: 50%; background: #fff url('/assets/images/tn-emblem.png') center / 42px no-repeat;
      box-shadow: 0 6px 18px -8px rgba(0,0,0,0.3); border: 1px solid rgba(124,58,237,0.12);
    }
    .auth-card h2 { margin: 0 0 8px; text-align: center; color: #1a1533; }
    .auth-card p { margin: 0 0 18px; text-align: center; color: #6b6485; }
    .auth-card form { display: flex; flex-direction: column; gap: 14px; margin-top: 10px; }
    .full-width { width: 100%; }
    .auth-links { display: flex; justify-content: space-between; flex-wrap: wrap; gap: 8px; margin-top: 18px; }
    .auth-links a, .back-link { color: #7c3aed; font-weight: 600; }
    .back-link { display: block; text-align: center; margin-top: 18px; }
  `]
})
export class LoginComponent {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly notification = inject(NotificationService);
  private readonly router = inject(Router);

  readonly loading = signal(false);
  readonly form: FormGroup = this.fb.group({
    // accepts an email OR a 10-digit Indian mobile number as the login identifier
    email: ['', [Validators.required, Validators.pattern(/^([^@\s]+@[^@\s]+\.[^@\s]+|[6-9]\d{9})$/)]],
    password: ['', [Validators.required, Validators.minLength(8)]]
  });

  async onSubmit(): Promise<void> {
    if (this.form.invalid) return;
    this.loading.set(true);
    try {
      const { email, password } = this.form.value;
      await this.authService.login(email, password);
      this.notification.success('Login successful');
      this.router.navigate(['/']);
    } catch (err) {
      this.notification.error('Invalid email or password');
    } finally {
      this.loading.set(false);
    }
  }
}
