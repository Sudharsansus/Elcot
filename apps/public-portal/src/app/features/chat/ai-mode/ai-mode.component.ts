// ============================================================
// AI MODE — command-palette overlay (the portal's primary AI surface).
// Presentation only: all intelligence + action execution lives in
// AiModeService. Opens via the nav-bar entry or the global Cmd/Ctrl-K.
// A11y: role=dialog + aria-modal, focus trap, ESC to close, aria-live
// transcript. Bilingual (Tamil / English). Voice input via Web Speech.
// ============================================================
import {
  Component, ChangeDetectionStrategy, inject, signal, ElementRef, viewChild,
  afterNextRender, effect, HostListener, OnDestroy,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AiModeService } from '../ai-mode.service';
import { I18nService } from '../../../core/services/i18n.service';

@Component({
  selector: 'app-ai-mode',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, FormsModule],
  templateUrl: './ai-mode.component.html',
  styleUrls: ['./ai-mode.component.scss'],
})
export class AiModeComponent implements OnDestroy {
  readonly ai = inject(AiModeService);
  private readonly i18n = inject(I18nService);

  readonly open = this.ai.open;
  readonly busy = this.ai.busy;
  readonly messages = this.ai.messages;
  readonly pending = this.ai.pending;
  readonly unavailable = this.ai.unavailable;
  readonly grounded = this.ai.grounded;
  readonly suggestions = this.ai.suggestions;
  readonly lang = this.i18n.currentLanguage;

  readonly draft = signal('');
  readonly listening = signal(false);
  readonly voiceSupported = signal(false);

  private readonly askInput = viewChild<ElementRef<HTMLInputElement>>('askInput');
  private readonly scroller = viewChild<ElementRef<HTMLElement>>('scroller');
  private readonly panel = viewChild<ElementRef<HTMLElement>>('panel');
  private recognition: { start(): void; stop(): void } | null = null;

  constructor() {
    afterNextRender(() => {
      const w = window as unknown as Record<string, unknown>;
      this.voiceSupported.set(!!(w['webkitSpeechRecognition'] || w['SpeechRecognition']));
    });
    // keep the transcript scrolled to the latest turn
    effect(() => { this.messages(); queueMicrotask(() => this.scrollToEnd()); });
    // move focus into the ask box whenever AI Mode opens
    effect(() => { if (this.open()) queueMicrotask(() => this.askInput()?.nativeElement.focus()); });
  }

  t(en: string, ta: string): string { return this.lang() === 'ta' ? ta : en; }

  /** Global shortcut: Cmd/Ctrl-K toggles AI Mode; ESC closes it. */
  @HostListener('document:keydown', ['$event'])
  onGlobalKey(e: KeyboardEvent): void {
    if ((e.metaKey || e.ctrlKey) && (e.key === 'k' || e.key === 'K')) {
      e.preventDefault();
      this.ai.toggle();
    } else if (e.key === 'Escape' && this.open()) {
      e.preventDefault();
      this.ai.close();
    }
  }

  submit(): void {
    const text = this.draft().trim();
    if (!text || this.busy()) return;
    this.draft.set('');
    void this.ai.run(text);
  }

  pick(q: string): void {
    if (this.busy()) return;
    this.draft.set('');
    void this.ai.run(q);
  }

  /** Focus trap: keep Tab within the dialog while it is open. */
  onPanelKeydown(e: KeyboardEvent): void {
    if (e.key !== 'Tab') return;
    const panel = this.panel()?.nativeElement;
    if (!panel) return;
    const nodes = panel.querySelectorAll<HTMLElement>(
      'button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])');
    const focusables = Array.from(nodes).filter((el) => !el.hasAttribute('disabled') && el.offsetParent !== null);
    if (!focusables.length) return;
    const first = focusables[0];
    const last = focusables[focusables.length - 1];
    const active = document.activeElement as HTMLElement | null;
    if (e.shiftKey && active === first) { e.preventDefault(); last.focus(); }
    else if (!e.shiftKey && active === last) { e.preventDefault(); first.focus(); }
  }

  // ---- voice input (talk to AI Mode) ----
  toggleVoice(): void {
    if (this.listening()) { this.stopListening(); return; }
    const w = window as unknown as Record<string, new () => Record<string, unknown>>;
    const SR = w['webkitSpeechRecognition'] || w['SpeechRecognition'];
    if (!SR) return;
    const rec = new SR() as unknown as Record<string, unknown> & { start(): void; stop(): void };
    rec['lang'] = this.lang() === 'ta' ? 'ta-IN' : 'en-IN';
    rec['interimResults'] = true;
    rec['continuous'] = false;
    rec['onresult'] = (ev: { resultIndex: number; results: ArrayLike<ArrayLike<{ transcript: string }> & { isFinal: boolean }> }) => {
      let final = '', interim = '';
      for (let i = ev.resultIndex; i < ev.results.length; i++) {
        const r = ev.results[i];
        if (r.isFinal) final += r[0].transcript; else interim += r[0].transcript;
      }
      this.draft.set((final || interim).trim());
      if (final) { this.stopListening(); this.submit(); }
    };
    rec['onerror'] = () => this.stopListening();
    rec['onend'] = () => this.listening.set(false);
    this.recognition = rec;
    this.listening.set(true);
    try { rec.start(); } catch { this.listening.set(false); }
  }
  private stopListening(): void {
    this.listening.set(false);
    try { this.recognition?.stop(); } catch { /* ignore */ }
  }

  private scrollToEnd(): void {
    const el = this.scroller()?.nativeElement;
    if (el) el.scrollTop = el.scrollHeight;
  }

  ngOnDestroy(): void { this.stopListening(); }
}
