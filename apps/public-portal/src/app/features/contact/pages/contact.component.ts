// ============================================================
// PUBLIC PORTAL — CONTACT PAGE
// ============================================================
// Drop into: apps/public-portal/src/app/features/contact/pages/ (new directory)
// ============================================================

import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { I18nService } from '../../../core/services/i18n.service';

@Component({
  selector: 'app-contact',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule, RouterModule, ReactiveFormsModule,
    MatIconModule, MatButtonModule, MatFormFieldModule, MatInputModule
  ],
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.scss']
})
export class ContactComponent implements OnInit {
  private readonly i18n = inject(I18nService);
  private readonly fb = inject(FormBuilder);

  readonly lang = this.i18n.currentLanguage;
  readonly submitted = signal(false);
  readonly sending = signal(false);

  readonly form: FormGroup = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(2)]],
    email: ['', [Validators.required, Validators.email]],
    phone: [''],
    subject: ['', [Validators.required, Validators.minLength(3)]],
    message: ['', [Validators.required, Validators.minLength(10)]]
  });

  get nameControl() { return this.form.get('name')!; }
  get emailControl() { return this.form.get('email')!; }
  get subjectControl() { return this.form.get('subject')!; }
  get messageControl() { return this.form.get('message')!; }

  ngOnInit(): void {
    // Form ready
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.sending.set(true);
    // No fake "message sent": compose a real email to the ELCOT facilitation
    // desk via the user's mail client. (When a backend /api/contact endpoint
    // exists, switch to a POST — but never claim a send that didn't happen.)
    const { name, email, phone, subject, message } = this.form.value;
    const to = 'facilitation@elcot.in';
    const body =
      `Name: ${name}\nEmail: ${email}\nPhone: ${phone || '-'}\n\n${message}`;
    const href = `mailto:${to}?subject=${encodeURIComponent(subject)}&body=${encodeURIComponent(body)}`;
    if (typeof window !== 'undefined') {
      window.location.href = href;
    }
    this.sending.set(false);
    this.submitted.set(true);
    this.form.reset();
  }

  t(key: string): string { return this.i18n.translate(key); }
}
