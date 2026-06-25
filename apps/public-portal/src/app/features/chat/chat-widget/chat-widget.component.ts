import {
  Component,
  inject,
  signal,
  computed,
  effect,
  ViewChild,
  ElementRef,
  AfterViewChecked,
  HostListener,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ChatService } from '../chat.service';

/**
 * Support contact shown when Mira cannot help and the user asks for a human.
 *
 * NOTE: phone is a PLACEHOLDER — replace `phone` with the real ELCOT helpline
 * number before Go-Live. Email/hours follow the portal's established conventions.
 */
const MIRA_SUPPORT_CONTACT = {
  phone: '1800-XXX-XXXX', // TODO(go-live): real ELCOT AVGC-XR helpline
  email: 'support@avgcxr.elcot.tn.gov.in',
  hoursEn: 'Mon–Fri, 10:00–18:00 IST',
  hoursTa: 'திங்கள்–வெள்ளி, காலை 10:00 – மாலை 18:00 IST',
} as const;

/**
 * Floating chat widget for the AVGC-XR Portal AI assistant (Mira).
 *
 * <p>Bilingual (English + Tamil). Renders a launcher button in the bottom-right
 * corner; clicking it opens a chat panel with message history, language toggle,
 * and an input box. Connects to the backend at /api/v1/chat.</p>
 *
 * <p>Phase-11 overhaul — accessibility + intelligence (NO backend change):
 * <ul>
 *   <li>WCAG: focus is moved into the panel on open and returned to the launcher
 *       on close; ESC closes; Tab is trapped inside the open panel; an
 *       {@code aria-live} region announces open/close, "Mira is typing" and each
 *       new assistant reply; the dialog is {@code aria-modal}.</li>
 *   <li>Escalation: if the user asks for a human (EN/TA keywords), a
 *       "Talk to a person" card with phone/email/hours is shown.</li>
 *   <li>Context-aware: a route-derived hint suggests relevant help on first open.</li>
 * </ul></p>
 */
@Component({
  selector: 'avgcxr-chat-widget',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chat-widget.component.html',
  styleUrl: './chat-widget.component.scss',
})
export class ChatWidgetComponent implements AfterViewChecked {
  readonly chat = inject(ChatService);
  private readonly router = inject(Router);

  @ViewChild('scrollContainer') private scrollContainer?: ElementRef<HTMLElement>;
  @ViewChild('chatInput') private chatInput?: ElementRef<HTMLTextAreaElement>;
  @ViewChild('panel') private panel?: ElementRef<HTMLElement>;
  @ViewChild('launcher') private launcher?: ElementRef<HTMLButtonElement>;

  private shouldScrollDown = true;
  /** id of the last assistant message already announced to screen readers */
  private lastAnnouncedId: string | null = null;
  /** tracks the previous open state so focus is only moved on real transitions */
  private wasOpen = false;

  readonly userInput = signal<string>('');
  /** Shows the "talk to a person" escalation card. */
  readonly escalation = signal<boolean>(false);
  /** Visually-hidden status text for the aria-live region. */
  readonly srStatus = signal<string>('');

  readonly contact = MIRA_SUPPORT_CONTACT;
  private readonly ta = computed(() => this.chat.language() === 'ta');

  readonly placeholderText = computed(() =>
    this.ta()
      ? 'ஏதாவது கேளுங்கள்... (Ask anything...)'
      : 'Ask about schemes, applications, status...'
  );

  readonly headerTitle = computed(() =>
    this.ta() ? 'மிரா - AI உதவியாளர்' : 'Mira -- AI Assistant'
  );

  readonly headerSubtitle = computed(() =>
    this.ta() ? 'தமிழ் நாடு AVGC-XR Portal' : 'Tamil Nadu AVGC-XR Portal'
  );

  readonly sendButtonText = computed(() => (this.ta() ? 'அனுப்பு' : 'Send'));

  readonly emptyStateText = computed(() =>
    this.ta()
      ? 'வணக்கம்! நான் மிரா. திட்டங்கள், விண்ணப்பங்கள், தகுதி பற்றி கேளுங்கள்.'
      : "Hi! I'm Mira. Ask me about schemes, applications, eligibility -- or write in Tamil."
  );

  readonly escalationTitle = computed(() =>
    this.ta() ? 'ஒரு நபருடன் பேசுங்கள்' : 'Talk to a person'
  );
  readonly escalationHint = computed(() =>
    this.ta()
      ? 'மிராவால் உதவ முடியாவிட்டால், எங்கள் ஆதரவு குழுவைத் தொடர்பு கொள்ளுங்கள்:'
      : "If Mira can't help, reach our support team:"
  );

  /** Route-aware help hint shown in the empty state (best-effort, additive). */
  readonly contextHint = computed<string>(() => {
    const url = this.router.url || '';
    const t = this.ta();
    if (url.includes('/schemes'))
      return t
        ? 'இந்தத் திட்டத்திற்கு விண்ணப்பிக்க உதவி வேண்டுமா?'
        : 'Need help applying for this scheme?';
    if (url.includes('/application'))
      return t
        ? 'உங்கள் விண்ணப்ப நிலையைச் சரிபார்க்க நான் உதவ முடியும்.'
        : 'I can help you check your application status.';
    if (url.includes('/contact') || url.includes('/grievance'))
      return t
        ? 'பொதுவான கேள்விகளுக்கு பதிலளிக்கிறேன் — அல்லது ஒரு நபருடன் இணைக்கிறேன்.'
        : 'I can answer common questions — or connect you to a person.';
    if (url.includes('/news') || url.includes('/event'))
      return t ? 'சமீபத்திய புதுப்பிப்புகள் பற்றி கேளுங்கள்.' : 'Ask me about the latest updates.';
    return '';
  });

  /** Suggested quick-replies (static fallback; backend /suggestions can extend later). */
  readonly suggestedQuestions = [
    { en: 'What schemes are available?', ta: 'என்ன திட்டங்கள் உள்ளன?' },
    { en: 'How do I apply?', ta: 'எப்படி விண்ணப்பிப்பது?' },
    { en: 'Eligibility criteria?', ta: 'தகுதி நிபந்தனைகள்?' },
    { en: 'Required documents?', ta: 'தேவையான ஆவணங்கள்?' },
  ];

  constructor() {
    // Move focus into the panel on open; return it to the launcher on close.
    // Guard on a real open<->close transition so we never steal focus on initial
    // render or on a language toggle while the panel is open.
    effect(() => {
      const open = this.chat.isOpen();
      if (open === this.wasOpen) return;
      this.wasOpen = open;
      const t = this.chat.language() === 'ta';
      // defer to after the @if view has (re)rendered
      setTimeout(() => {
        if (open) {
          this.chatInput?.nativeElement.focus();
          this.srStatus.set(t ? 'மிரா திறக்கப்பட்டது' : 'Mira assistant opened');
        } else {
          this.launcher?.nativeElement.focus();
          this.srStatus.set(t ? 'மிரா மூடப்பட்டது' : 'Mira assistant closed');
          this.escalation.set(false);
        }
      });
    });

    // Announce "typing" and each new assistant reply to screen readers.
    effect(() => {
      if (this.chat.isLoading()) {
        this.srStatus.set(this.ta() ? 'மிரா பதிலளிக்கிறது' : 'Mira is typing');
        return;
      }
      const msgs = this.chat.messages();
      const last = msgs[msgs.length - 1];
      if (last && last.role === 'ASSISTANT' && last.id !== this.lastAnnouncedId) {
        this.lastAnnouncedId = last.id;
        this.srStatus.set(last.content);
      }
    });
  }

  ngAfterViewChecked(): void {
    if (this.shouldScrollDown && this.scrollContainer) {
      const el = this.scrollContainer.nativeElement;
      el.scrollTop = el.scrollHeight;
      this.shouldScrollDown = false;
    }
  }

  send(): void {
    const text = this.userInput();
    if (!text.trim()) return;
    if (this.isEscalationRequest(text)) {
      this.escalation.set(true);
    }
    this.userInput.set('');
    this.shouldScrollDown = true;
    this.chat.sendMessage(text).subscribe();
  }

  sendSuggestion(text: string): void {
    this.userInput.set(text);
    this.send();
  }

  dismissEscalation(): void {
    this.escalation.set(false);
  }

  onKeydown(ev: KeyboardEvent): void {
    if (ev.key === 'Enter' && !ev.shiftKey) {
      ev.preventDefault();
      this.send();
    }
  }

  toggleLanguage(): void {
    this.chat.setLanguage(this.chat.language() === 'en' ? 'ta' : 'en');
  }

  /** ESC closes the open panel; Tab is trapped within it (WCAG 2.1.2 / 2.4.3). */
  @HostListener('document:keydown', ['$event'])
  onDocumentKeydown(ev: KeyboardEvent): void {
    if (!this.chat.isOpen()) return;
    if (ev.key === 'Escape') {
      ev.preventDefault();
      this.chat.close();
      return;
    }
    if (ev.key === 'Tab') {
      this.trapFocus(ev);
    }
  }

  private trapFocus(ev: KeyboardEvent): void {
    const root = this.panel?.nativeElement;
    if (!root) return;
    const focusable = Array.from(
      root.querySelectorAll<HTMLElement>(
        'a[href], button:not([disabled]), textarea:not([disabled]), input:not([disabled]), [tabindex]:not([tabindex="-1"])'
      )
    ).filter((el) => el.offsetParent !== null);
    if (focusable.length === 0) return;
    const first = focusable[0];
    const last = focusable[focusable.length - 1];
    const active = document.activeElement as HTMLElement | null;
    if (ev.shiftKey && active === first) {
      ev.preventDefault();
      last.focus();
    } else if (!ev.shiftKey && active === last) {
      ev.preventDefault();
      first.focus();
    }
  }

  private isEscalationRequest(text: string): boolean {
    const t = text.toLowerCase();
    const en =
      /\b(agent|human|person|someone|somebody|representative|customer\s*care|help\s*desk|talk to (a )?(person|human|someone|representative))\b/;
    if (en.test(t)) return true;
    const taWords = ['மனிதர்', 'அதிகாரி', 'பணியாளர்', 'நபர்', 'உதவி மையம்', 'ஆதரவு'];
    return taWords.some((w) => text.includes(w));
  }

  trackByIdx(i: number): number {
    return i;
  }
}
