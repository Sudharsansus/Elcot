// ============================================================
// PUBLIC PORTAL — CONTACT PAGE
// ============================================================
// Drop into: apps/public-portal/src/app/features/contact/pages/ (new directory)
// ============================================================

import { Component, ChangeDetectionStrategy, inject, signal, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { I18nService } from '../../../core/services/i18n.service';
import { FormAssistService, AssistSpec } from '../../chat/form-assist.service';

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
export class ContactComponent implements OnInit, OnDestroy {
  private readonly i18n = inject(I18nService);
  private readonly fb = inject(FormBuilder);
  private readonly formAssist = inject(FormAssistService);
  private spec!: AssistSpec;

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
    this.spec = {
      titleEn: 'message',
      titleTa: 'செய்தி',
      fields: [
        { key: 'name', labelEn: 'What is your name?', labelTa: 'உங்கள் பெயர் என்ன?',
          validate: (v) => (v.trim().length >= 2 ? null : 'your name (at least 2 letters)') },
        { key: 'email', labelEn: 'Your email address?', labelTa: 'உங்கள் மின்னஞ்சல் முகவரி என்ன?', type: 'email',
          validate: (v) => (/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(v.trim()) ? null : 'a valid email like name@example.com') },
        { key: 'phone', labelEn: 'Your phone number? (optional — say "skip" to leave it blank)',
          labelTa: 'தொலைபேசி எண்? (விருப்பம் — "skip" எனச் சொல்லவும்)', type: 'tel' },
        { key: 'subject', labelEn: 'What is it about? (a short subject)', labelTa: 'எதைப் பற்றி? (சுருக்கமான தலைப்பு)',
          validate: (v) => (v.trim().length >= 3 ? null : 'a short subject (at least 3 letters)') },
        { key: 'message', labelEn: 'What would you like to tell us?', labelTa: 'நீங்கள் என்ன சொல்ல விரும்புகிறீர்கள்?',
          validate: (v) => (v.trim().length >= 10 ? null : 'a message of at least 10 characters') },
      ],
      setValue: (k, val) => {
        let v = val;
        if (k === 'phone' && /^\s*skip\s*$/i.test(val)) v = '';
        this.form.get(k)?.setValue(v);
        this.form.get(k)?.markAsTouched();
      },
      getValue: (k) => this.form.get(k)?.value ?? '',
      submit: () => this.onSubmit(),
    };
    this.formAssist.register(this.spec);
  }

  ngOnDestroy(): void { this.formAssist.clear(this.spec); }

  fillWithMira(): void { this.formAssist.begin(); }

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
