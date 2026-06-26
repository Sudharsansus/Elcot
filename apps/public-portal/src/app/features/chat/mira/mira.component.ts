// ============================================================
// MIRA — agentic voice assistant for the AVGC-XR portal.
// • Client-side "agent": detects intent and TAKES ACTIONS (navigate,
//   open the Scheme Finder, jump to directories) against the live app.
// • Answers from a local knowledge base; open questions fall back to the
//   live backend chat API (RAG) via ChatService.askBackend().
// • Voice in (Web Speech SpeechRecognition) + voice out (SpeechSynthesis).
// Browser-only, graceful when speech APIs are unavailable.
// ============================================================
import {
  Component, ChangeDetectionStrategy, inject, signal, ElementRef, viewChild,
  afterNextRender, effect, OnDestroy,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { ChatService } from '../chat.service';
import { SmoothScrollService } from '../../../core/services/smooth-scroll.service';
import { I18nService } from '../../../core/services/i18n.service';
import { SCHEMES_DATA, SECTOR_CATEGORIES } from '../../schemes/schemes.data';

type IntentKind = 'navigate' | 'finder' | 'answer';
interface Intent { kind: IntentKind; route?: string; reply: string; backend?: boolean; }

@Component({
  selector: 'app-mira',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, FormsModule],
  templateUrl: './mira.component.html',
  styleUrls: ['./mira.component.scss'],
})
export class MiraComponent implements OnDestroy {
  private readonly chat = inject(ChatService);
  private readonly router = inject(Router);
  private readonly smooth = inject(SmoothScrollService);
  private readonly i18n = inject(I18nService);

  readonly open = this.chat.isOpen;
  readonly messages = this.chat.messages;
  readonly lang = this.i18n.currentLanguage;
  readonly draft = signal('');
  readonly listening = signal(false);
  readonly speakOn = signal(false);
  readonly thinking = signal(false);
  readonly voiceSupported = signal(false);

  private recognition: { start(): void; stop(): void } | null = null;
  private readonly scroller = viewChild<ElementRef<HTMLElement>>('scroller');

  readonly quickPrompts = [
    { en: 'Find me a scheme', ta: 'எனக்கேற்ற திட்டம்', q: 'find me a scheme' },
    { en: 'Animation incentives', ta: 'அனிமேஷன் ஊக்கம்', q: 'animation schemes' },
    { en: 'How do I apply?', ta: 'எப்படி விண்ணப்பிப்பது?', q: 'how do i apply' },
    { en: 'What is this portal?', ta: 'இந்த இணையதளம்?', q: 'what is this portal' },
  ];

  constructor() {
    afterNextRender(() => {
      const w = window as unknown as Record<string, unknown>;
      this.voiceSupported.set(!!(w['webkitSpeechRecognition'] || w['SpeechRecognition']));
      this.loadVoices();
    });
    effect(() => { this.messages(); queueMicrotask(() => this.scrollToEnd()); });
  }

  private t(en: string, ta: string): string { return this.lang() === 'ta' ? ta : en; }

  toggle(): void {
    this.chat.toggle();
    if (this.chat.isOpen() && this.messages().length === 0) {
      this.chat.push('ASSISTANT', this.greeting());
    }
  }
  close(): void { this.chat.close(); this.stopListening(); this.stopSpeaking(); }

  onQuick(q: string): void { if (!this.thinking()) this.handle(q); }

  submit(): void {
    const text = this.draft().trim();
    if (!text || this.thinking()) return;
    this.draft.set('');
    void this.handle(text);
  }

  private async handle(text: string): Promise<void> {
    this.chat.push('USER', text);
    const intent = this.detect(text);
    this.thinking.set(true);

    let reply = intent.reply;
    if (intent.backend) {
      const r = await firstValueFrom(this.chat.askBackend(text)).catch(() => null);
      if (r && r.trim()) reply = r.trim();
    } else {
      await this.delay(420);
    }

    this.thinking.set(false);
    this.chat.push('ASSISTANT', reply);
    this.speak(reply);
    this.act(intent);
  }

  private act(intent: Intent): void {
    if (intent.kind === 'navigate' && intent.route) {
      void this.router.navigateByUrl(intent.route);
      setTimeout(() => this.chat.close(), 650);
    } else if (intent.kind === 'finder') {
      const go = () => setTimeout(() => this.smooth.scrollTo('#scheme-finder', -90), 450);
      if (this.router.url.split('?')[0] !== '/') void this.router.navigateByUrl('/').then(go);
      else go();
      setTimeout(() => this.chat.close(), 750);
    }
  }

  // ---- agentic intent detection ----
  private detect(raw: string): Intent {
    const x = ` ${raw.toLowerCase()} `;
    const has = (...k: string[]) => k.some((s) => x.includes(s));

    const sector = SECTOR_CATEGORIES.find(
      (s) => x.includes(s.labelEn.toLowerCase()) || x.includes(s.key.toLowerCase()) || raw.includes(s.labelTa),
    );
    if (sector && has('scheme', 'incentive', 'grant', 'subsid', 'fund', 'support')) {
      return { kind: 'navigate', route: '/schemes', reply: this.t(
        `Here are the incentives relevant to ${sector.labelEn}. Opening the schemes catalogue — use the filters to narrow by category and funding.`,
        `${sector.labelTa} தொடர்பான ஊக்கத்திட்டங்கள் இதோ. திட்டப் பட்டியலைத் திறக்கிறேன்.`) };
    }
    if (has('find', 'eligib', 'which scheme', 'suitable', 'match', 'recommend', 'for me', 'qualify')) {
      return { kind: 'finder', reply: this.t(
        "Let's match you to the right scheme. Opening the Scheme Finder — choose your profile (studio, individual or student), sector and stage, and I'll filter the incentives you qualify for.",
        'சரியான திட்டத்தைப் பொருத்துவோம். Scheme Finder-ஐத் திறக்கிறேன் — உங்கள் சுயவிவரத்தைத் தேர்ந்தெடுங்கள்.') };
    }
    if (has('scheme', 'incentive', 'grant', 'subsid', 'funding', 'financial')) {
      const names = SCHEMES_DATA.slice(0, 3).map((s) => '• ' + s.name).join('\n');
      return { kind: 'navigate', route: '/schemes', reply: this.t(
        `There are ${SCHEMES_DATA.length} active incentive schemes — production subsidy, training, XR infrastructure, market access, freelancer support and scholarships. For example:\n${names}\nOpening the full catalogue now.`,
        `${SCHEMES_DATA.length} செயலில் உள்ள ஊக்கத்திட்டங்கள் உள்ளன. முழுப் பட்டியலைத் திறக்கிறேன்.`) };
    }
    if (has('apply', 'how do i', 'register', 'sign up', 'signup', 'enrol', 'create account')) {
      return { kind: 'navigate', route: '/auth/register', reply: this.t(
        'Applying is fully online in 5 steps: register, check eligibility, submit your form & documents, departmental review, then sanction & disbursal. Taking you to registration to create your applicant profile.',
        'விண்ணப்பம் முழுவதும் ஆன்லைனில் — 5 படிகள். பதிவுக்கு அழைத்துச் செல்கிறேன்.') };
    }
    if (has('compan', 'studio', 'business connect')) {
      return { kind: 'navigate', route: '/companies', reply: this.t('Opening Business Connect — the directory of AVGC-XR companies and studios across Tamil Nadu.', 'Business Connect-ஐத் திறக்கிறேன்.') };
    }
    if (has('talent', ' job', 'intern', 'career', 'hire', 'recruit')) {
      return { kind: 'navigate', route: '/talent', reply: this.t('Opening Talent Connect — the skill registry of students and professionals.', 'Talent Connect-ஐத் திறக்கிறேன்.') };
    }
    if (has('freelanc')) {
      return { kind: 'navigate', route: '/freelancers', reply: this.t('Opening the Freelancer Registry.', 'Freelancer Registry-ஐத் திறக்கிறேன்.') };
    }
    if (has('event', 'news', 'workshop', 'webinar')) {
      return { kind: 'navigate', route: '/events', reply: this.t('Opening News & Events.', 'செய்திகள் & நிகழ்வுகளைத் திறக்கிறேன்.') };
    }
    if (has('resource', 'document', 'download', 'guideline')) {
      return { kind: 'navigate', route: '/resources', reply: this.t('Opening Resources — policy documents, guidelines and downloads.', 'வளங்களைத் திறக்கிறேன்.') };
    }
    if (has('contact', 'reach', 'phone', ' call', 'email', 'address', 'helpline')) {
      return { kind: 'navigate', route: '/contact', reply: this.t('You can reach ELCOT at 044 2254 0100 or info@elcot.in. Opening the contact page.', 'ELCOT: 044 2254 0100 / info@elcot.in. தொடர்பு பக்கம்.') };
    }
    if (has('about', 'what is this', 'who runs', 'who is behind', 'elcot', 'purpose')) {
      return { kind: 'answer', reply: this.t(
        'This is the official Government of Tamil Nadu portal for the AVGC-XR sector — Animation, VFX, Gaming, Comics and Extended Reality — run by ELCOT. Discover incentive schemes, apply online, and connect with studios, talent and freelancers. What would you like to do?',
        'இது தமிழ்நாடு அரசின் அதிகாரப்பூர்வ AVGC-XR இணையதளம், ELCOT-ஆல் நிர்வகிக்கப்படுகிறது. நீங்கள் என்ன செய்ய விரும்புகிறீர்கள்?') };
    }
    if (has('hello', ' hi ', ' hey', 'vanakkam', 'வணக்கம்', 'help', 'what can you')) {
      return { kind: 'answer', reply: this.greeting() };
    }
    return { kind: 'answer', backend: true, reply: this.t(
      "I can help you find schemes, check eligibility, apply, or jump to studios, talent, freelancers, events or contact. Try “find me a scheme” or “how do I apply?”",
      'திட்டங்கள், தகுதி, விண்ணப்பம் அல்லது எந்தப் பகுதிக்கும் உதவ முடியும்.') };
  }

  private greeting(): string {
    return this.t(
      "Hi, I'm Mira — your guide to the Tamil Nadu AVGC-XR portal. I can find government schemes, check what you're eligible for, help you apply, and take you anywhere on the site. Ask me, or tap the mic to talk.",
      'வணக்கம், நான் Mira — தமிழ்நாடு AVGC-XR இணையதளத்திற்கான வழிகாட்டி. திட்டங்கள், தகுதி, விண்ணப்பம் — கேளுங்கள், அல்லது மைக்கைத் தட்டுங்கள்.');
  }

  // ---- voice input ----
  toggleVoice(): void {
    if (this.listening()) { this.stopListening(); return; }
    const w = window as unknown as Record<string, new () => Record<string, unknown>>;
    const SR = w['webkitSpeechRecognition'] || w['SpeechRecognition'];
    if (!SR) return;
    const rec = new SR() as unknown as Record<string, unknown> & { start(): void; stop(): void };
    rec['lang'] = this.lang() === 'ta' ? 'ta-IN' : 'en-IN';
    rec['interimResults'] = true;
    rec['continuous'] = false;
    rec['onresult'] = (e: { resultIndex: number; results: ArrayLike<ArrayLike<{ transcript: string }> & { isFinal: boolean }> }) => {
      let final = '', interim = '';
      for (let i = e.resultIndex; i < e.results.length; i++) {
        const r = e.results[i];
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

  // ---- voice output (female voice, EN/TA selectable) ----
  private voices: SpeechSynthesisVoice[] = [];
  // name fragments that identify a female/girl voice across OSes & languages
  private static readonly FEMALE_HINTS = [
    'female', 'samantha', 'zira', 'veena', 'raveena', 'heera', 'pallavi',
    'kalpana', 'asha', 'swara', 'neerja', 'aditi', 'sangeeta', 'tessa',
    'fiona', 'karen', 'serena', 'moira', 'google uk english female', 'google தமிழ்',
  ];

  private loadVoices(): void {
    if (typeof speechSynthesis === 'undefined') return;
    const grab = () => { this.voices = speechSynthesis.getVoices() ?? []; };
    grab();
    try { speechSynthesis.onvoiceschanged = grab; } catch { /* ignore */ }
  }

  /** Pick a female voice for the language, preferring an Indian-region match. */
  private pickVoice(lang: 'en' | 'ta'): SpeechSynthesisVoice | null {
    if (!this.voices.length) return null;
    const pref = lang === 'ta' ? 'ta' : 'en';
    const pool = this.voices.filter((v) => v.lang?.toLowerCase().startsWith(pref));
    const list = pool.length ? pool : this.voices;
    const region = lang === 'ta' ? 'ta-in' : 'en-in';
    const isFemale = (v: SpeechSynthesisVoice) =>
      MiraComponent.FEMALE_HINTS.some((h) => v.name.toLowerCase().includes(h));
    return (
      list.find((v) => isFemale(v) && v.lang?.toLowerCase() === region) ||
      list.find((v) => isFemale(v)) ||
      list.find((v) => v.lang?.toLowerCase() === region) ||
      list[0] || null
    );
  }

  /** Choose Mira's language — switches her text replies AND her voice together. */
  setVoiceLang(lang: 'en' | 'ta'): void {
    if (this.lang() === lang) return;
    void this.i18n.setLanguage(lang);
  }

  toggleSpeak(): void {
    this.speakOn.update((v) => !v);
    if (!this.speakOn()) this.stopSpeaking();
  }
  private speak(text: string): void {
    if (!this.speakOn() || typeof speechSynthesis === 'undefined') return;
    try {
      speechSynthesis.cancel();
      const u = new SpeechSynthesisUtterance(text.replace(/[•*_#` ]/g, ' ').slice(0, 420));
      const voice = this.pickVoice(this.lang() === 'ta' ? 'ta' : 'en');
      if (voice) u.voice = voice;
      u.lang = this.lang() === 'ta' ? 'ta-IN' : 'en-IN';
      u.rate = 1.0;
      u.pitch = 1.18; // brighter, younger female timbre
      speechSynthesis.speak(u);
    } catch { /* ignore */ }
  }
  private stopSpeaking(): void {
    try { if (typeof speechSynthesis !== 'undefined') speechSynthesis.cancel(); } catch { /* ignore */ }
  }

  private scrollToEnd(): void {
    const el = this.scroller()?.nativeElement;
    if (el) el.scrollTop = el.scrollHeight;
  }
  private delay(ms: number): Promise<void> { return new Promise((r) => setTimeout(r, ms)); }

  ngOnDestroy(): void { this.stopListening(); this.stopSpeaking(); }
}
