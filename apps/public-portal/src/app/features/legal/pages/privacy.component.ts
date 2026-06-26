import { Component, ChangeDetectionStrategy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { I18nService } from '../../../core/services/i18n.service';

/**
 * Privacy Policy page (GIGW / IT Act aligned) for the Tamil Nadu AVGC-XR Portal.
 * The portal collects personal data at registration and application time, so a
 * published privacy notice is a compliance requirement, not optional.
 */
@Component({
  selector: 'app-privacy',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, RouterModule],
  template: `
    <main class="legal">
      <div class="legal-inner">
        <nav class="crumb"><a routerLink="/">Home</a> <span>›</span> Privacy Policy</nav>

        <header class="legal-head">
          <h1>Privacy Policy</h1>
          <p class="ta">தனியுரிமைக் கொள்கை</p>
          <p class="meta">
            Tamil Nadu AVGC-XR Portal · Electronics Corporation of Tamil Nadu (ELCOT),
            Government of Tamil Nadu · Last updated: 26 June 2026
          </p>
        </header>

        <section>
          <h2>1. Introduction</h2>
          <p>
            This Privacy Policy explains how the Tamil Nadu AVGC-XR Portal ("the Portal"),
            operated by ELCOT on behalf of the Government of Tamil Nadu, collects, uses, stores
            and protects your personal information when you register, apply for schemes, or
            otherwise use the Portal. By using the Portal you consent to the practices described
            here.
          </p>
        </section>

        <section>
          <h2>2. Information We Collect</h2>
          <ul>
            <li>
              <strong>Account information</strong> — name, email address, mobile number,
              district, department/designation (where applicable) and your password (stored only
              as a one-way salted hash, never in plain text).
            </li>
            <li>
              <strong>Application information</strong> — scheme application details, company /
              project information, and documents you upload (e.g. PDFs, images) in support of an
              application.
            </li>
            <li>
              <strong>Technical information</strong> — IP address, browser type, and access logs,
              collected automatically to keep the service secure and reliable.
            </li>
          </ul>
        </section>

        <section>
          <h2>3. How We Use Your Information</h2>
          <ul>
            <li>To create and manage your account and authenticate you securely.</li>
            <li>To receive, review, and process your scheme applications.</li>
            <li>To send you status updates, notifications, and grievance responses.</li>
            <li>To produce aggregate, non-identifying statistics for programme administration.</li>
            <li>To detect, prevent, and respond to fraud, abuse, and security incidents.</li>
          </ul>
        </section>

        <section>
          <h2>4. Legal Basis</h2>
          <p>
            We process your data on the basis of your consent and in furtherance of the statutory
            and administrative functions of ELCOT and the Government of Tamil Nadu in delivering
            AVGC-XR sector schemes and incentives.
          </p>
        </section>

        <section>
          <h2>5. Storage &amp; Security</h2>
          <p>
            Your data is stored on access-controlled servers and transmitted only over encrypted
            (HTTPS/TLS) connections. Passwords are stored as salted BCrypt hashes. Access to
            personal data is restricted to authorised officials on a need-to-know basis. While we
            apply reasonable security safeguards, no method of transmission or storage is
            completely secure.
          </p>
        </section>

        <section>
          <h2>6. Sharing of Information</h2>
          <p>
            We do <strong>not</strong> sell or rent your personal data. Information may be shared
            with relevant departments and agencies of the Government of Tamil Nadu strictly for
            processing your applications and for grievance redressal, or where required by law.
          </p>
        </section>

        <section>
          <h2>7. Data Retention</h2>
          <p>
            We retain your information for as long as your account is active and for the period
            required to administer the schemes and to comply with applicable record-keeping,
            audit, and legal obligations, after which it is securely deleted or anonymised.
          </p>
        </section>

        <section>
          <h2>8. Your Rights</h2>
          <p>
            You may access and correct your account information from your profile, and you may
            request deletion of your account or raise a concern about how your data is handled by
            contacting the Grievance Officer below.
          </p>
        </section>

        <section>
          <h2>9. Cookies &amp; Local Storage</h2>
          <p>
            The Portal stores a session token in your browser's local storage to keep you signed
            in. It is used only for authentication and is removed when you sign out. We do not use
            third-party advertising or tracking cookies.
          </p>
        </section>

        <section>
          <h2>10. Grievance Officer</h2>
          <p>
            For any privacy-related questions or grievances, please reach out through the Portal's
            <a routerLink="/grievance">grievance and contact page</a>, or write to the Electronics
            Corporation of Tamil Nadu (ELCOT), Chennai, Tamil Nadu.
          </p>
        </section>

        <section>
          <h2>11. Changes to This Policy</h2>
          <p>
            We may update this Privacy Policy from time to time. Material changes will be reflected
            on this page along with a revised "Last updated" date.
          </p>
        </section>
      </div>
    </main>
  `,
  styles: [
    `
      .legal {
        padding: 7rem 1.5rem 5rem;
        background: var(--color-background, #f7f5fd);
        min-height: 100vh;
      }
      .legal-inner {
        max-width: 820px;
        margin: 0 auto;
        background: #fff;
        border: 1px solid rgba(124, 58, 237, 0.12);
        border-radius: 18px;
        padding: clamp(1.5rem, 4vw, 3rem);
        box-shadow: 0 20px 60px -30px rgba(69, 18, 92, 0.35);
      }
      .crumb {
        font-size: 0.85rem;
        color: #6b7280;
        margin-bottom: 1.25rem;
      }
      .crumb a {
        color: var(--color-primary, #7c3aed);
        text-decoration: none;
      }
      .crumb span {
        margin: 0 0.4rem;
      }
      .legal-head {
        border-bottom: 1px solid rgba(124, 58, 237, 0.12);
        padding-bottom: 1.5rem;
        margin-bottom: 1.5rem;
      }
      .legal-head h1 {
        font-size: clamp(1.8rem, 4vw, 2.5rem);
        margin: 0 0 0.25rem;
        color: #45125c;
      }
      .legal-head .ta {
        font-family: 'Noto Sans Tamil', sans-serif;
        font-size: 1.1rem;
        color: var(--color-primary, #7c3aed);
        margin: 0 0 0.75rem;
      }
      .legal-head .meta {
        font-size: 0.85rem;
        color: #6b7280;
        margin: 0;
        line-height: 1.5;
      }
      section {
        margin: 1.75rem 0;
      }
      h2 {
        font-size: 1.15rem;
        color: #45125c;
        margin: 0 0 0.6rem;
      }
      p,
      li {
        color: #374151;
        line-height: 1.7;
        font-size: 0.97rem;
      }
      ul {
        padding-left: 1.25rem;
        margin: 0.5rem 0;
      }
      li {
        margin-bottom: 0.5rem;
      }
      a {
        color: var(--color-primary, #7c3aed);
      }
    `,
  ],
})
export class PrivacyComponent {
  // Exposed for future bilingual rendering; current content is English-first.
  protected readonly lang = inject(I18nService).currentLanguage;
}
