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
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  template: `
    <div class="auth-container">
      <div class="auth-card">
        <h2>Reset Password</h2>
        <p>Enter your email address to receive a password reset link.</p>
        <form [formGroup]="form" (ngSubmit)="onSubmit()">
          <mat-form-field appearance="outline" class="full-width">
            <mat-label>Email</mat-label>
            <input matInput formControlName="email" type="email" />
          </mat-form-field>
          <button mat-raised-button color="primary" type="submit" [disabled]="loading()">Send Reset Link</button>
        </form>
        <a routerLink="/auth/login" class="back-link">Back to Login</a>
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
      width: 100%; max-width: 440px;
    }
    .auth-card::before {
      content: ''; display: block; width: 64px; height: 64px; margin: 0 auto 16px;
      border-radius: 50%; background: #fff url('/assets/images/tn-emblem.png') center / 42px no-repeat;
      box-shadow: 0 6px 18px -8px rgba(0,0,0,0.3); border: 1px solid rgba(124,58,237,0.12);
    }
    .auth-card h2 { margin: 0 0 8px; text-align: center; color: #1a1533; }
    .auth-card p { margin: 0 0 18px; text-align: center; color: #6b6485; }
    .auth-card form { display: flex; flex-direction: column; gap: 14px; margin-top: 6px; }
    .full-width { width: 100%; }
    .back-link { display: block; text-align: center; margin-top: 18px; color: #7c3aed; font-weight: 600; }
  `]
})
export class ForgotPasswordComponent {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly notification = inject(NotificationService);
  private readonly router = inject(Router);

  readonly loading = signal(false);
  readonly form: FormGroup = this.fb.group({
    email: ['', [Validators.required, Validators.email]]
  });

  async onSubmit(): Promise<void> {
    if (this.form.invalid) return;
    this.loading.set(true);
    try {
      await this.authService.forgotPassword(this.form.value.email);
      this.notification.success('Password reset link sent to your email');
      this.router.navigate(['/auth/login']);
    } catch {
      this.notification.error('Failed to send reset link');
    } finally {
      this.loading.set(false);
    }
  }
}
