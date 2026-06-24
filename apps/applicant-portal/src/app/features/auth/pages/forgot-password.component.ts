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
    .auth-container { display: flex; align-items: center; justify-content: center; min-height: calc(100vh - 200px); padding: var(--spacing-xl); }
    .auth-card { background: var(--color-surface); padding: var(--spacing-xxl); border-radius: var(--radius-lg); box-shadow: var(--shadow-lg); width: 100%; max-width: 440px; }
    .auth-card h2 { margin-bottom: var(--spacing-sm); text-align: center; }
    .auth-card p { margin-bottom: var(--spacing-lg); text-align: center; color: var(--color-text-secondary); }
    .auth-card form { display: flex; flex-direction: column; gap: var(--spacing-md); }
    .back-link { display: block; text-align: center; margin-top: var(--spacing-lg); }
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
