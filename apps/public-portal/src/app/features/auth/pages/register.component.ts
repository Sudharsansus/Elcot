import { Component, ChangeDetectionStrategy, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../../core/services/auth.service';
import { NotificationService } from '../../../core/services/notification.service';
import { I18nService } from '../../../core/services/i18n.service';
import { AuthShellComponent } from '../auth-shell.component';

@Component({
  selector: 'app-register',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatIconModule, AuthShellComponent],
  template: `
    <app-auth-shell
      [title]="ta() ? 'கணக்கை உருவாக்குங்கள்' : 'Create your account'"
      [subtitle]="ta() ? 'பதிவுசெய்து தமிழ்நாட்டின் AVGC-XR ஊக்கத்திட்டங்களுக்கு விண்ணப்பிக்கவும்.' : 'Register to apply for Tamil Nadu’s AVGC-XR incentives.'"
      [points]="points()">
      <h2 class="auth-heading">{{ ta() ? 'பதிவு' : 'Register' }}</h2>
      <p class="auth-subheading">{{ ta() ? 'தொடங்க உங்கள் விவரங்களை வழங்கவும்.' : 'Provide your details to get started.' }}</p>

      <form [formGroup]="form" (ngSubmit)="onSubmit()" class="auth-form">
        <mat-form-field appearance="outline">
          <mat-label>{{ ta() ? 'முழு பெயர்' : 'Full name' }}</mat-label>
          <input matInput formControlName="name" autocomplete="name" />
          @if (form.get('name')?.touched && form.get('name')?.invalid) {
            <mat-error>{{ ta() ? 'பெயர் தேவை' : 'Name is required' }}</mat-error>
          }
        </mat-form-field>

        <div class="auth-grid-2">
          <mat-form-field appearance="outline">
            <mat-label>{{ ta() ? 'மின்னஞ்சல்' : 'Email' }}</mat-label>
            <input matInput formControlName="email" type="email" autocomplete="email" />
            @if (form.get('email')?.touched && form.get('email')?.invalid) {
              <mat-error>{{ ta() ? 'சரியான மின்னஞ்சல் தேவை' : 'Valid email required' }}</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>{{ ta() ? 'தொலைபேசி' : 'Phone' }}</mat-label>
            <input matInput formControlName="phone" type="tel" autocomplete="tel" maxlength="10" />
            @if (form.get('phone')?.touched && form.get('phone')?.invalid) {
              <mat-error>{{ ta() ? '10 இலக்க எண்' : '10-digit number' }}</mat-error>
            }
          </mat-form-field>
        </div>

        <mat-form-field appearance="outline">
          <mat-label>{{ ta() ? 'நீங்கள் யார்?' : 'I am a…' }}</mat-label>
          <mat-select formControlName="role">
            <mat-option value="APPLICANT">{{ ta() ? 'தனிநபர் / ஃப்ரீலான்ஸர்' : 'Individual / Freelancer' }}</mat-option>
            <mat-option value="COMPANY_REP">{{ ta() ? 'நிறுவன பிரதிநிதி' : 'Company representative' }}</mat-option>
          </mat-select>
        </mat-form-field>

        <div class="auth-grid-2">
          <mat-form-field appearance="outline">
            <mat-label>{{ ta() ? 'கடவுச்சொல்' : 'Password' }}</mat-label>
            <input matInput formControlName="password" type="password" autocomplete="new-password" />
            @if (form.get('password')?.touched && form.get('password')?.invalid) {
              <mat-error>{{ ta() ? 'குறைந்தது 8 எழுத்துகள்' : 'Min 8 characters' }}</mat-error>
            }
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>{{ ta() ? 'கடவுச்சொல்லை உறுதிப்படுத்து' : 'Confirm password' }}</mat-label>
            <input matInput formControlName="confirmPassword" type="password" autocomplete="new-password" />
          </mat-form-field>
        </div>

        <button type="submit" class="auth-submit" [disabled]="loading()">
          <mat-icon aria-hidden="true">person_add</mat-icon>
          {{ loading() ? (ta() ? 'உருவாக்குகிறது…' : 'Creating…') : (ta() ? 'கணக்கை உருவாக்கு' : 'Create account') }}
        </button>
      </form>

      <div class="auth-links" style="justify-content:center;">
        <a routerLink="/auth/login">{{ ta() ? 'ஏற்கனவே கணக்கு உள்ளதா? உள்நுழைக' : 'Already have an account? Sign in' }}</a>
      </div>
    </app-auth-shell>
  `,
})
export class RegisterComponent {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly notification = inject(NotificationService);
  private readonly router = inject(Router);
  private readonly i18n = inject(I18nService);

  readonly ta = computed(() => this.i18n.currentLanguage() === 'ta');
  readonly loading = signal(false);
  readonly points = computed(() => this.ta()
    ? ['இலவச பதிவு', 'அனைத்து திட்டங்களுக்கும் ஒரே கணக்கு', 'பாதுகாப்பான & சரிபார்க்கப்பட்ட']
    : ['Free to register', 'One account for every scheme', 'Secure & verified']);

  readonly form: FormGroup = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(2)]],
    email: ['', [Validators.required, Validators.email]],
    phone: ['', [Validators.required, Validators.pattern(/^[6-9]\d{9}$/)]],
    role: ['APPLICANT', Validators.required],
    password: ['', [Validators.required, Validators.minLength(8)]],
    confirmPassword: ['', Validators.required],
  });

  async onSubmit(): Promise<void> {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    if (this.form.value.password !== this.form.value.confirmPassword) {
      this.notification.error(this.ta() ? 'கடவுச்சொற்கள் பொருந்தவில்லை' : 'Passwords do not match');
      return;
    }
    this.loading.set(true);
    try {
      const { confirmPassword, ...data } = this.form.value;
      await this.authService.register(data);
      this.notification.success(this.ta() ? 'பதிவு வெற்றி. உங்கள் மின்னஞ்சலைச் சரிபார்க்கவும்.' : 'Registration successful. Please verify your email.');
      this.router.navigate(['/auth/login']);
    } catch {
      this.notification.error(this.ta() ? 'பதிவு தோல்வியடைந்தது. மீண்டும் முயற்சிக்கவும்.' : 'Registration failed. Please try again.');
    } finally {
      this.loading.set(false);
    }
  }
}
