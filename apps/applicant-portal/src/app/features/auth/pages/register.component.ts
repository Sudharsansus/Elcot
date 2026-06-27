import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../../core/services/auth.service';
import { NotificationService } from '../../../core/services/notification.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule],
  template: `
    <div class="auth-container">
      <div class="auth-card">
        <h2>Create Account</h2>
        <form [formGroup]="form" (ngSubmit)="onSubmit()">
          <mat-form-field appearance="outline">
            <mat-label>Full Name</mat-label>
            <input matInput formControlName="name" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Email</mat-label>
            <input matInput formControlName="email" type="email" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Phone</mat-label>
            <input matInput formControlName="phone" type="tel" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Role</mat-label>
            <mat-select formControlName="role">
              <mat-option value="APPLICANT">Individual / Freelancer</mat-option>
              <mat-option value="COMPANY_REP">Company Representative</mat-option>
            </mat-select>
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Password</mat-label>
            <input matInput formControlName="password" type="password" />
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Confirm Password</mat-label>
            <input matInput formControlName="confirmPassword" type="password" />
          </mat-form-field>
          <button mat-raised-button color="primary" type="submit" [disabled]="loading()" class="full-width">
            {{ loading() ? 'Creating...' : 'Create Account' }}
          </button>
        </form>
        <div class="auth-links" style="justify-content:center; margin-top: var(--spacing-md);">
          <a routerLink="/auth/login">Already have an account? Sign In</a>
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
      width: 100%; max-width: 480px;
    }
    .auth-card::before {
      content: ''; display: block; width: 64px; height: 64px; margin: 0 auto 16px;
      border-radius: 50%; background: #fff url('/assets/images/tn-emblem.png') center / 42px no-repeat;
      box-shadow: 0 6px 18px -8px rgba(0,0,0,0.3); border: 1px solid rgba(124,58,237,0.12);
    }
    .auth-card h2 { margin: 0 0 8px; text-align: center; color: #1a1533; }
    .auth-card form { display: flex; flex-direction: column; gap: 12px; margin-top: 10px; }
    .full-width { width: 100%; }
    .auth-links { display: flex; justify-content: center; margin-top: 18px; }
    .auth-links a { color: #7c3aed; font-weight: 600; }
  `]
})
export class RegisterComponent {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly notification = inject(NotificationService);
  private readonly router = inject(Router);

  readonly loading = signal(false);
  readonly form: FormGroup = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(2)]],
    email: ['', [Validators.required, Validators.email]],
    phone: ['', [Validators.required, Validators.pattern(/^[6-9]\\d{9}$/)]],
    role: ['APPLICANT', Validators.required],
    password: ['', [Validators.required, Validators.minLength(8)]],
    confirmPassword: ['', Validators.required]
  });

  async onSubmit(): Promise<void> {
    if (this.form.invalid) return;
    if (this.form.value.password !== this.form.value.confirmPassword) {
      this.notification.error('Passwords do not match');
      return;
    }
    this.loading.set(true);
    try {
      const { confirmPassword, ...data } = this.form.value;
      await this.authService.register(data);
      this.notification.success('Registration successful. Please verify your email.');
      this.router.navigate(['/auth/login']);
    } catch (err) {
      this.notification.error('Registration failed. Please try again.');
    } finally {
      this.loading.set(false);
    }
  }
}
