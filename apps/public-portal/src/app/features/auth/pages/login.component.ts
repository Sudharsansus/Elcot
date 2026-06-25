import { Component, ChangeDetectionStrategy, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../../core/services/auth.service';
import { NotificationService } from '../../../core/services/notification.service';
import { I18nService } from '../../../core/services/i18n.service';
import { AuthShellComponent } from '../auth-shell.component';

@Component({
  selector: 'app-login',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatIconModule, MatButtonModule, AuthShellComponent],
  template: `
    <app-auth-shell
      [title]="ta() ? 'மீண்டும் வரவேற்கிறோம்' : 'Welcome back'"
      [subtitle]="ta() ? 'உங்கள் விண்ணப்பங்கள் மற்றும் திட்ட நிலையை நிர்வகிக்க உள்நுழையவும்.' : 'Sign in to manage your applications and track scheme status.'"
      [points]="points()">
      <h2 class="auth-heading">{{ ta() ? 'உள்நுழைவு' : 'Sign in' }}</h2>
      <p class="auth-subheading">{{ ta() ? 'உங்கள் கணக்கு விவரங்களை உள்ளிடவும்.' : 'Enter your account details to continue.' }}</p>

      <form [formGroup]="form" (ngSubmit)="onSubmit()" class="auth-form">
        <mat-form-field appearance="outline">
          <mat-label>{{ ta() ? 'மின்னஞ்சல்' : 'Email' }}</mat-label>
          <input matInput formControlName="email" type="email" autocomplete="email" />
          @if (form.get('email')?.touched && form.get('email')?.hasError('required')) {
            <mat-error>{{ ta() ? 'மின்னஞ்சல் தேவை' : 'Email is required' }}</mat-error>
          }
          @if (form.get('email')?.hasError('email')) {
            <mat-error>{{ ta() ? 'சரியான மின்னஞ்சலை உள்ளிடவும்' : 'Enter a valid email' }}</mat-error>
          }
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>{{ ta() ? 'கடவுச்சொல்' : 'Password' }}</mat-label>
          <input matInput formControlName="password" [type]="show() ? 'text' : 'password'" autocomplete="current-password" />
          <button mat-icon-button matSuffix type="button" (click)="show.set(!show())"
                  [attr.aria-label]="show() ? 'Hide password' : 'Show password'">
            <mat-icon>{{ show() ? 'visibility_off' : 'visibility' }}</mat-icon>
          </button>
          @if (form.get('password')?.touched && form.get('password')?.hasError('required')) {
            <mat-error>{{ ta() ? 'கடவுச்சொல் தேவை' : 'Password is required' }}</mat-error>
          }
        </mat-form-field>

        <button type="submit" class="auth-submit" [disabled]="loading()">
          <mat-icon aria-hidden="true">login</mat-icon>
          {{ loading() ? (ta() ? 'உள்நுழைகிறது…' : 'Signing in…') : (ta() ? 'உள்நுழைக' : 'Sign in') }}
        </button>
      </form>

      <div class="auth-links">
        <a routerLink="/auth/forgot-password">{{ ta() ? 'கடவுச்சொல் மறந்துவிட்டதா?' : 'Forgot password?' }}</a>
        <a routerLink="/auth/register">{{ ta() ? 'புதிய கணக்கை உருவாக்கு' : 'Create an account' }}</a>
      </div>
    </app-auth-shell>
  `,
})
export class LoginComponent {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly notification = inject(NotificationService);
  private readonly router = inject(Router);
  private readonly i18n = inject(I18nService);

  readonly ta = computed(() => this.i18n.currentLanguage() === 'ta');
  readonly loading = signal(false);
  readonly show = signal(false);
  readonly points = computed(() => this.ta()
    ? ['அனைத்து திட்டங்களுக்கும் ஒற்றை உள்நுழைவு', 'விண்ணப்ப நிலையை நேரலையில் கண்காணிக்கவும்', 'பாதுகாப்பான அரசு போர்ட்டல்']
    : ['One sign-in for every scheme', 'Track your application status live', 'Secure government portal']);

  readonly form: FormGroup = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
  });

  async onSubmit(): Promise<void> {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.loading.set(true);
    try {
      const { email, password } = this.form.value;
      await this.authService.login(email, password);
      this.notification.success(this.ta() ? 'வெற்றிகரமாக உள்நுழைந்தீர்கள்' : 'Signed in successfully');
      this.router.navigate(['/']);
    } catch {
      this.notification.error(this.ta() ? 'தவறான மின்னஞ்சல் அல்லது கடவுச்சொல்' : 'Invalid email or password');
    } finally {
      this.loading.set(false);
    }
  }
}
