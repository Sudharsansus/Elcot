import { Component, ChangeDetectionStrategy, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../../core/services/auth.service';
import { NotificationService } from '../../../core/services/notification.service';
import { I18nService } from '../../../core/services/i18n.service';
import { AuthShellComponent } from '../auth-shell.component';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatIconModule, AuthShellComponent],
  template: `
    <app-auth-shell
      [title]="ta() ? 'கடவுச்சொல்லை மீட்டமைக்கவும்' : 'Reset your password'"
      [subtitle]="ta() ? 'உங்கள் பதிவுசெய்த மின்னஞ்சலுக்கு மீட்டமைப்பு இணைப்பை அனுப்புவோம்.' : 'We’ll send a reset link to your registered email address.'">
      <h2 class="auth-heading">{{ ta() ? 'கடவுச்சொல் மீட்டமைப்பு' : 'Forgot password' }}</h2>
      <p class="auth-subheading">{{ ta() ? 'மீட்டமைப்பு இணைப்பைப் பெற உங்கள் மின்னஞ்சலை உள்ளிடவும்.' : 'Enter your email to receive a password reset link.' }}</p>

      <form [formGroup]="form" (ngSubmit)="onSubmit()" class="auth-form">
        <mat-form-field appearance="outline">
          <mat-label>{{ ta() ? 'மின்னஞ்சல்' : 'Email' }}</mat-label>
          <input matInput formControlName="email" type="email" autocomplete="email" />
          @if (form.get('email')?.touched && form.get('email')?.invalid) {
            <mat-error>{{ ta() ? 'சரியான மின்னஞ்சல் தேவை' : 'Valid email required' }}</mat-error>
          }
        </mat-form-field>

        <button type="submit" class="auth-submit" [disabled]="loading()">
          <mat-icon aria-hidden="true">mail</mat-icon>
          {{ loading() ? (ta() ? 'அனுப்புகிறது…' : 'Sending…') : (ta() ? 'மீட்டமைப்பு இணைப்பை அனுப்பு' : 'Send reset link') }}
        </button>
      </form>

      <div class="auth-links" style="justify-content:center;">
        <a routerLink="/auth/login">{{ ta() ? '← உள்நுழைவுக்குத் திரும்பு' : '← Back to sign in' }}</a>
      </div>
    </app-auth-shell>
  `,
})
export class ForgotPasswordComponent {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly notification = inject(NotificationService);
  private readonly router = inject(Router);
  private readonly i18n = inject(I18nService);

  readonly ta = computed(() => this.i18n.currentLanguage() === 'ta');
  readonly loading = signal(false);
  readonly form: FormGroup = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
  });

  async onSubmit(): Promise<void> {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.loading.set(true);
    try {
      await this.authService.forgotPassword(this.form.value.email);
      this.notification.success(this.ta() ? 'மீட்டமைப்பு இணைப்பு உங்கள் மின்னஞ்சலுக்கு அனுப்பப்பட்டது' : 'Password reset link sent to your email');
      this.router.navigate(['/auth/login']);
    } catch {
      this.notification.error(this.ta() ? 'மீட்டமைப்பு இணைப்பை அனுப்ப முடியவில்லை' : 'Failed to send reset link');
    } finally {
      this.loading.set(false);
    }
  }
}
