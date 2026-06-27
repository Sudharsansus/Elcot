import { Component, ChangeDetectionStrategy, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

/**
 * Applicant profile — personal details, account verification (email + mobile)
 * and KYC (Aadhaar + PAN via DigiLocker).
 *
 * NOTE: verification and DigiLocker linking here are DEMONSTRATIONS of the
 * service flow (no real OTP / Aadhaar exchange). The OTP step activates once the
 * SMS/email provider is configured; the DigiLocker step becomes a real Meripehchaan
 * OAuth flow once the portal is onboarded as a DigiLocker requester post-award.
 */
@Component({
  selector: 'app-profile',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent {
  readonly districts = [
    'Chennai', 'Coimbatore', 'Madurai', 'Tiruchirappalli', 'Salem',
    'Tirunelveli', 'Erode', 'Vellore', 'Thanjavur', 'Tiruppur',
  ];

  profileForm = {
    fullName: 'Demo Applicant',
    fullNameTamil: '',
    email: 'applicant@example.com',
    mobile: '9000000001',
    dob: '',
    district: 'Chennai',
    address: '',
  };
  readonly saving = signal(false);

  // ---- Verification (demo) ----
  readonly emailVerified = signal(false);
  readonly mobileVerified = signal(false);
  readonly otp = signal<{ open: boolean; channel: 'email' | 'mobile'; sending: boolean; code: string }>(
    { open: false, channel: 'email', sending: false, code: '' },
  );

  // ---- DigiLocker KYC (demo) ----
  readonly aadhaarLinked = signal(false);
  readonly aadhaarMasked = signal('');
  readonly panNumber = signal('');
  readonly dl = signal<{ open: boolean; step: 'consent' | 'loading' }>({ open: false, step: 'consent' });

  saveProfile(): void {
    this.saving.set(true);
    setTimeout(() => this.saving.set(false), 700);
  }

  // ---- Verification demo flow ----
  startVerify(channel: 'email' | 'mobile'): void {
    this.otp.set({ open: true, channel, sending: true, code: '' });
    // Simulate "sending the OTP" — real delivery activates with the SMS/email provider.
    setTimeout(() => this.otp.update((o) => ({ ...o, sending: false })), 900);
  }
  setOtpCode(v: string): void { this.otp.update((o) => ({ ...o, code: v })); }
  confirmOtp(): void {
    const o = this.otp();
    if (o.code.replace(/\D/g, '').length < 4) return;
    if (o.channel === 'email') this.emailVerified.set(true);
    else this.mobileVerified.set(true);
    this.otp.set({ open: false, channel: o.channel, sending: false, code: '' });
  }
  cancelOtp(): void { this.otp.update((o) => ({ ...o, open: false })); }

  // ---- DigiLocker demo flow ----
  openDigilocker(): void { this.dl.set({ open: true, step: 'consent' }); }
  digilockerAllow(): void {
    this.dl.set({ open: true, step: 'loading' });
    setTimeout(() => {
      this.aadhaarMasked.set('XXXX XXXX 4321');
      this.panNumber.set('ABCDE1234F');
      this.aadhaarLinked.set(true);
      this.dl.set({ open: false, step: 'consent' });
    }, 1500);
  }
  digilockerDeny(): void { this.dl.set({ open: false, step: 'consent' }); }
}
