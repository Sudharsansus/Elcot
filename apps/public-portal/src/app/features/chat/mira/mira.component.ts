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
  Component, ChangeDetectionStrategy, inject, signal, computed, ElementRef, viewChild,
  afterNextRender, effect, untracked, OnDestroy,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, NavigationEnd } from '@angular/router';
import { filter, firstValueFrom, map } from 'rxjs';
import { ChatService, AgentAction } from '../chat.service';
import { SmoothScrollService } from '../../../core/services/smooth-scroll.service';
import { I18nService } from '../../../core/services/i18n.service';
import { FormAssistService } from '../form-assist.service';
import { SCHEMES_DATA, SECTOR_CATEGORIES, Scheme } from '../../schemes/schemes.data';

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
  private readonly formAssist = inject(FormAssistService);

  readonly open = this.chat.isOpen;
  readonly messages = this.chat.messages;
  readonly lang = this.i18n.currentLanguage;
  readonly draft = signal('');
  readonly listening = signal(false);
  readonly speakOn = signal(false);
  readonly thinking = signal(false);
  readonly speaking = signal(false);
  readonly filling = signal(false);
  readonly voiceSupported = signal(false);
  readonly tamilVoiceAvailable = signal(false);

  private formStep = -1;
  private awaitingSubmit = false;

  private recognition: { start(): void; stop(): void } | null = null;
  private readonly scroller = viewChild<ElementRef<HTMLElement>>('scroller');

  private readonly routeUrl = toSignal(
    this.router.events.pipe(
      filter((e): e is NavigationEnd => e instanceof NavigationEnd),
      map(() => this.router.url),
    ),
    { initialValue: this.router.url },
  );

  /** Prebuilt commands — context-aware to the page the user is on. */
  readonly quickPrompts = computed(() => this.promptsFor(this.routeUrl()));
  readonly showSuggestions = signal(true);
  toggleSuggestions(): void { this.showSuggestions.update((v) => !v); }

  constructor() {
    afterNextRender(() => {
      const w = window as unknown as Record<string, unknown>;
      this.voiceSupported.set(!!(w['webkitSpeechRecognition'] || w['SpeechRecognition']));
      this.loadVoices();
    });
    effect(() => { this.messages(); queueMicrotask(() => this.scrollToEnd()); });
    // "Fill with Mira" tapped on a form → start the guided fill
    effect(() => {
      const n = this.formAssist.startTick();
      if (n > 0) untracked(() => this.startFormFill());
    });
  }

  private t(en: string, ta: string): string { return this.lang() === 'ta' ? ta : en; }

  toggle(): void {
    this.chat.toggle();
    if (this.chat.isOpen()) {
      this.showSuggestions.set(true); // re-surface the (context-aware) commands on open
      if (this.messages().length === 0) this.chat.push('ASSISTANT', this.greeting());
    }
  }
  close(): void { this.chat.close(); this.stopListening(); this.stopSpeaking(); this.resetFill(); }

  // ---- page awareness: Mira knows which page you're on ----
  private pageName(): string {
    const u = (this.router.url.split('?')[0] || '/').toLowerCase();
    const map: Record<string, [string, string]> = {
      '/schemes': ['Schemes', 'திட்டங்கள்'],
      '/companies': ['Business Connect', 'பிசினஸ் கனெக்ட்'],
      '/talent': ['Talent Connect', 'டேலன்ட் கனெக்ட்'],
      '/freelancers': ['Freelancer Registry', 'ஃப்ரீலான்சர்'],
      '/events': ['News & Events', 'செய்திகள் & நிகழ்வுகள்'],
      '/resources': ['Resources', 'வளங்கள்'],
      '/about': ['About', 'பற்றி'],
      '/contact': ['Contact', 'தொடர்பு'],
      '/auth': ['sign-in', 'உள்நுழைவு'],
    };
    const key = Object.keys(map).find((k) => u.startsWith(k));
    if (key) return this.lang() === 'ta' ? map[key][1] : map[key][0];
    return u === '/' ? (this.lang() === 'ta' ? 'முகப்பு' : 'Home') : '';
  }

  /** Context-aware prebuilt commands for the page the user is on. */
  private promptsFor(url: string): { en: string; ta: string; q: string }[] {
    const u = (url.split('?')[0] || '/').toLowerCase();
    const findScheme = { en: 'Find me a scheme', ta: 'திட்டத்தைக் கண்டறி', q: 'find me a scheme' };
    if (u.startsWith('/schemes')) return [
      { en: 'Check eligibility', ta: 'தகுதியைச் சரிபார்', q: 'find me a scheme' },
      { en: 'Required documents', ta: 'தேவையான ஆவணங்கள்', q: 'what documents do i need' },
      { en: 'Application deadlines', ta: 'விண்ணப்ப காலக்கெடு', q: 'deadlines' },
      { en: 'How do I apply?', ta: 'எப்படி விண்ணப்பிப்பது?', q: 'how do i apply' },
      { en: 'List all schemes', ta: 'அனைத்து திட்டங்கள்', q: 'list all schemes' },
    ];
    if (u.startsWith('/companies')) return [
      { en: 'Register my company', ta: 'நிறுவனத்தைப் பதிவு செய்', q: 'how do i apply' },
      findScheme,
      { en: 'Talent Connect', ta: 'டேலன்ட் கனெக்ட்', q: 'open talent connect' },
      { en: 'Contact ELCOT', ta: 'ELCOT தொடர்பு', q: 'contact' },
    ];
    if (u.startsWith('/talent')) return [
      { en: 'Register as talent', ta: 'திறமையாளராகப் பதிவு', q: 'how do i apply' },
      { en: 'Freelancer Registry', ta: 'ஃப்ரீலான்சர் பதிவு', q: 'open freelancers' },
      findScheme,
    ];
    if (u.startsWith('/freelancers')) return [
      { en: 'Register as freelancer', ta: 'ஃப்ரீலான்சராகப் பதிவு', q: 'how do i apply' },
      { en: 'Talent Connect', ta: 'டேலன்ட் கனெக்ட்', q: 'open talent connect' },
      findScheme,
    ];
    if (u.startsWith('/auth/register')) return [
      { en: '✨ Fill this form with Mira', ta: '✨ Mira-வுடன் நிரப்பு', q: 'fill the form' },
      { en: 'What documents do I need?', ta: 'தேவையான ஆவணங்கள்?', q: 'what documents do i need' },
      { en: 'How do I apply?', ta: 'எப்படி விண்ணப்பிப்பது?', q: 'how do i apply' },
    ];
    if (u.startsWith('/contact') || u.startsWith('/grievance')) return [
      { en: '✨ Fill this form with Mira', ta: '✨ Mira-வுடன் நிரப்பு', q: 'fill the form' },
      { en: 'Helpline number', ta: 'உதவி எண்', q: 'contact' },
      findScheme,
    ];
    if (u.startsWith('/events')) return [
      { en: 'Latest events', ta: 'சமீபத்திய நிகழ்வுகள்', q: 'open events' },
      findScheme,
      { en: 'How do I apply?', ta: 'எப்படி விண்ணப்பிப்பது?', q: 'how do i apply' },
    ];
    if (u.startsWith('/resources')) return [
      { en: 'Policy documents', ta: 'கொள்கை ஆவணங்கள்', q: 'open resources' },
      findScheme,
      { en: 'Required documents', ta: 'தேவையான ஆவணங்கள்', q: 'what documents do i need' },
    ];
    // default — home and everything else
    return [
      { en: '✨ Find me a scheme', ta: '✨ எனக்கேற்ற திட்டம்', q: 'find me a scheme' },
      { en: 'List all schemes', ta: 'அனைத்து திட்டங்கள்', q: 'list all schemes' },
      { en: 'How do I apply?', ta: 'எப்படி விண்ணப்பிப்பது?', q: 'how do i apply' },
      { en: 'Required documents', ta: 'தேவையான ஆவணங்கள்', q: 'what documents do i need' },
      { en: 'Application deadlines', ta: 'விண்ணப்ப காலக்கெடு', q: 'deadlines' },
      { en: 'Business Connect', ta: 'பிசினஸ் கனெக்ட்', q: 'open business connect' },
      { en: 'Talent Connect', ta: 'டேலன்ட் கனெக்ட்', q: 'open talent connect' },
      { en: 'Contact ELCOT', ta: 'ELCOT தொடர்பு', q: 'contact' },
    ];
  }

  /** Format the scheme list (optionally filtered by category) for the chat. */
  private schemeList(category?: Scheme['category']): string {
    const list = category ? SCHEMES_DATA.filter((s) => s.category === category) : SCHEMES_DATA;
    const use = list.length ? list : SCHEMES_DATA;
    return use
      .map((s) => {
        const nm = this.lang() === 'ta' ? s.nameTa : s.name;
        const amt = s.maxAmount >= 100000 ? `₹${Math.round(s.maxAmount / 100000)}L` : `₹${s.maxAmount}`;
        return `• ${nm} — ${this.t('up to', 'அதிகபட்சம்')} ${amt}`;
      })
      .join('\n');
  }

  onQuick(q: string): void {
    if (this.thinking()) return;
    this.showSuggestions.set(false);
    if (q === 'fill the form') { this.formAssist.begin(); return; }
    void this.handle(q);
  }

  submit(): void {
    const text = this.draft().trim();
    if (!text || this.thinking()) return;
    this.draft.set('');
    if (this.formStep >= 0 || this.awaitingSubmit) {
      this.chat.push('USER', text);
      this.handleFormInput(text);
      return;
    }
    this.showSuggestions.set(false);
    void this.handle(text);
  }

  // ---- guided "Fill with Mira" flow ----
  private startFormFill(): void {
    const spec = this.formAssist.active();
    if (!spec) return;
    if (!this.chat.isOpen()) this.chat.toggle();
    this.formStep = 0;
    this.awaitingSubmit = false;
    this.filling.set(true);
    this.chat.push('ASSISTANT', this.t(
      `Sure — let's fill the ${spec.titleEn} together. I'll ask one thing at a time; reply here and I'll fill it in (and flag anything that looks off).`,
      `சரி — ${spec.titleTa} படிவத்தை ஒன்றாக நிரப்புவோம். ஒவ்வொன்றாகக் கேட்கிறேன்; இங்கே பதிலளியுங்கள், நான் நிரப்புகிறேன்.`));
    this.askField(0);
  }
  private askField(i: number): void {
    const spec = this.formAssist.active();
    if (!spec) return;
    const f = spec.fields[i];
    let q = this.lang() === 'ta' ? f.labelTa : f.labelEn;
    if (f.type === 'select' && f.options) q += `\n(${f.options.join('  •  ')})`;
    this.chat.push('ASSISTANT', q);
  }
  private handleFormInput(text: string): void {
    const spec = this.formAssist.active();
    if (!spec) { this.resetFill(); return; }

    if (this.awaitingSubmit) {
      this.awaitingSubmit = false;
      this.filling.set(false);
      if (/\b(yes|submit|ok|okay|go|create|sure|ஆம்|சரி|சமர்ப்)/i.test(text)) {
        this.chat.push('ASSISTANT', this.t('Submitting…', 'சமர்ப்பிக்கிறேன்…'));
        spec.submit?.();
      } else {
        this.chat.push('ASSISTANT', this.t(
          "No problem — the form's filled in. Review it and submit whenever you're ready.",
          'பரவாயில்லை — படிவம் நிரப்பப்பட்டது. சரிபார்த்து சமர்ப்பிக்கவும்.'));
      }
      return;
    }

    const i = this.formStep;
    const f = spec.fields[i];
    let value = text.trim();
    if (f.type === 'select' && f.options) {
      const m = f.options.find(
        (o) => o.toLowerCase().includes(value.toLowerCase())
          || value.toLowerCase().includes(o.split(' ')[0].toLowerCase()));
      value = m ?? value;
    }
    const err = f.validate ? f.validate(value) : null;
    if (err) {
      this.chat.push('ASSISTANT', this.t(
        `Hmm — I need ${err}. Let's try that one again:`,
        `சரியில்லை — ${err} தேவை. மீண்டும் முயற்சிக்கவும்:`));
      this.askField(i);
      return;
    }
    spec.setValue(f.key, value);
    const next = i + 1;
    if (next < spec.fields.length) {
      this.formStep = next;
      this.chat.push('ASSISTANT', this.t('Got it ✓', 'சரி ✓'));
      this.askField(next);
    } else {
      this.formStep = -1;
      this.awaitingSubmit = true;
      this.chat.push('ASSISTANT', this.t(
        "All set ✓ — I've filled the form. Want me to submit it now? (yes / no)",
        'அனைத்தும் தயார் ✓ — படிவத்தை நிரப்பிவிட்டேன். இப்போது சமர்ப்பிக்கவா? (ஆம்/இல்லை)'));
    }
  }
  private resetFill(): void {
    this.formStep = -1;
    this.awaitingSubmit = false;
    this.filling.set(false);
  }

  private async handle(text: string): Promise<void> {
    this.chat.push('USER', text);
    const intent = this.detect(text);
    this.thinking.set(true);

    let reply = intent.reply;
    let action: AgentAction | null = null;
    if (intent.backend) {
      const r = await firstValueFrom(this.chat.askBackend(text)).catch(() => null);
      if (r) {
        const br = (r.reply ?? '').trim();
        // Only let the backend REPLACE Mira's helpful local reply when it gives a
        // substantive answer — never override with a short "can't help" refusal
        // (which the rule-based backend returns until the LLM is configured).
        const unhelpful =
          !br || br.length < 18 ||
          /can'?t help|cannot help|couldn'?t help|unable to help|i'?m not sure|don'?t know|trouble reaching/i.test(br);
        if (br && !unhelpful) reply = br;
        action = r.action;
      }
    } else {
      await this.delay(420);
    }

    this.thinking.set(false);
    this.chat.push('ASSISTANT', reply);
    this.speak(reply);
    // The LLM agent's chosen action takes precedence over the rule-based intent.
    if (action) this.runAction(action);
    else this.act(intent);
  }

  /** Execute an action the LLM agent returned (validated server-side). */
  private runAction(action: AgentAction): void {
    const args = action.args ?? {};
    switch (action.tool) {
      case 'navigate': {
        const route = String(args['route'] ?? '');
        if (route.startsWith('/')) {
          void this.router.navigateByUrl(route);
          setTimeout(() => this.chat.close(), 650);
        }
        break;
      }
      case 'openSchemeFinder': {
        const go = () => setTimeout(() => this.smooth.scrollTo('#scheme-finder', -90), 450);
        if (this.router.url.split('?')[0] !== '/') void this.router.navigateByUrl('/').then(go);
        else go();
        setTimeout(() => this.chat.close(), 750);
        break;
      }
      case 'fillForm': {
        const route = String(args['form'] ?? '') === 'contact' ? '/contact' : '/auth/register';
        const begin = () => setTimeout(() => this.formAssist.begin(), 500);
        if (this.router.url.split('?')[0] !== route) void this.router.navigateByUrl(route).then(begin);
        else begin();
        break;
      }
      case 'findScheme': {
        const c = String(args['category'] ?? '') as Scheme['category'];
        const valid = ['production', 'training', 'infrastructure', 'export', 'freelancer', 'scholarship'].includes(c);
        this.chat.push('ASSISTANT', this.schemeList(valid ? c : undefined));
        break;
      }
      case 'listSchemes': {
        this.chat.push('ASSISTANT', this.schemeList());
        break;
      }
    }
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

    // map a category keyword in the message to a scheme bucket (for filtered lists)
    const catKey: Record<string, Scheme['category']> = {
      subsid: 'production', production: 'production',
      training: 'training', skill: 'training', course: 'training',
      infrastructure: 'infrastructure', 'studio setup': 'infrastructure', setup: 'infrastructure',
      export: 'export', 'market access': 'export', international: 'export',
      freelanc: 'freelancer', scholar: 'scholarship', student: 'scholarship',
    };
    const catHit = Object.keys(catKey).find((k) => x.includes(k));
    const cat = catHit ? catKey[catHit] : undefined;

    if (has('list scheme', 'what scheme', 'all scheme', 'available scheme', 'schemes are',
            'show scheme', 'scheme list', 'what are the scheme', 'list of scheme', 'see scheme',
            'schemes do you', 'schemes you have')) {
      return { kind: 'answer', reply: this.t(
        `${cat ? 'Matching schemes' : `There are ${SCHEMES_DATA.length} active incentive schemes`}:\n${this.schemeList(cat)}\nAsk about any one for eligibility & documents, or say "open schemes" for the full catalogue.`,
        `${cat ? 'பொருந்தும் திட்டங்கள்' : `${SCHEMES_DATA.length} செயலில் உள்ள திட்டங்கள்`}:\n${this.schemeList(cat)}\nஏதேனும் ஒன்றைப் பற்றிக் கேளுங்கள், அல்லது "open schemes" எனச் சொல்லுங்கள்.`) };
    }
    if (has('deadline', 'last date', 'due date', 'closing date', 'when to apply', 'when does it close', 'application date')) {
      const lines = SCHEMES_DATA.map((s) => `• ${this.lang() === 'ta' ? s.nameTa : s.name}: ${s.deadline}`).join('\n');
      return { kind: 'answer', reply: this.t(`Application deadlines:\n${lines}`, `விண்ணப்ப காலக்கெடு:\n${lines}`) };
    }
    if (has('document', 'what do i need', 'papers', 'what to submit', 'certificate')) {
      return { kind: 'navigate', route: '/schemes', reply: this.t(
        'Typical documents: company registration, GST & PAN, a project proposal, budget, audited financials and a portfolio. Each scheme lists its exact set — opening the catalogue so you can check the one you want.',
        'பொதுவான ஆவணங்கள்: நிறுவன பதிவு, GST & PAN, திட்ட முன்மொழிவு, பட்ஜெட், தணிக்கை அறிக்கைகள், போர்ட்ஃபோலியோ. திட்டப் பட்டியலைத் திறக்கிறேன்.') };
    }
    if (has('status', 'track', 'my application', 'application status', 'where is my', 'check my')) {
      return { kind: 'navigate', route: '/auth/login', reply: this.t(
        'Sign in to track your application status on your dashboard. Taking you to the login page.',
        'உங்கள் டாஷ்போர்டில் விண்ணப்ப நிலையைக் காண உள்நுழையவும். உள்நுழைவுப் பக்கம்.') };
    }

    if (has('scheme', 'incentive', 'grant', 'subsid', 'funding', 'financial')) {
      if (cat) {
        return { kind: 'answer', reply: this.t(
          `Matching schemes:\n${this.schemeList(cat)}\nAsk about any one for eligibility & documents, or say "open schemes".`,
          `பொருந்தும் திட்டங்கள்:\n${this.schemeList(cat)}`) };
      }
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
    const page = this.pageName();
    const enPage = page ? ` I see you're on the ${page} page — I can help with that, or anything else.` : '';
    const taPage = page ? ` நீங்கள் ${page} பக்கத்தில் உள்ளீர்கள் — அதற்கும் உதவ முடியும்.` : '';
    return this.t(
      `Hi, I'm Mira — your guide to the Tamil Nadu AVGC-XR portal.${enPage} I can find schemes, check what you're eligible for, help you apply, and take you anywhere on the site. Ask me, or tap the mic to talk.`,
      `வணக்கம், நான் Mira — தமிழ்நாடு AVGC-XR இணையதளத்திற்கான வழிகாட்டி.${taPage} திட்டங்கள், தகுதி, விண்ணப்பம் — கேளுங்கள், அல்லது மைக்கைத் தட்டுங்கள்.`);
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
    'fiona', 'karen', 'serena', 'moira', 'google uk english female',
    'google தமிழ்', 'tamil', 'valluvar', 'pooja',
  ];

  private loadVoices(): void {
    if (typeof speechSynthesis === 'undefined') return;
    const grab = () => {
      this.voices = speechSynthesis.getVoices() ?? [];
      this.tamilVoiceAvailable.set(
        this.voices.some((v) => v.lang?.toLowerCase().startsWith('ta')),
      );
    };
    grab();
    try { speechSynthesis.onvoiceschanged = grab; } catch { /* ignore */ }
  }

  /**
   * Pick the best voice for the language (female + Indian-region preferred).
   * Tamil text is ONLY ever spoken by a Tamil voice — never an English fallback,
   * which would mangle the phonetics. If no Tamil voice is installed this returns
   * null, and {@link speak} still tags the utterance ta-IN so OS engines that can
   * synthesise Tamil by language code (e.g. Android) speak it correctly.
   */
  private pickVoice(lang: 'en' | 'ta'): SpeechSynthesisVoice | null {
    if (!this.voices.length) return null;
    const isFemale = (v: SpeechSynthesisVoice) =>
      MiraComponent.FEMALE_HINTS.some((h) => v.name.toLowerCase().includes(h));

    if (lang === 'ta') {
      const ta = this.voices.filter((v) => v.lang?.toLowerCase().startsWith('ta'));
      if (!ta.length) return null;
      return (
        ta.find((v) => v.lang?.toLowerCase() === 'ta-in' && isFemale(v)) ||
        ta.find((v) => v.lang?.toLowerCase() === 'ta-in') ||
        ta.find(isFemale) ||
        ta[0]
      );
    }

    const en = this.voices.filter((v) => v.lang?.toLowerCase().startsWith('en'));
    const list = en.length ? en : this.voices;
    return (
      list.find((v) => isFemale(v) && v.lang?.toLowerCase() === 'en-in') ||
      list.find((v) => isFemale(v)) ||
      list.find((v) => v.lang?.toLowerCase() === 'en-in') ||
      list[0] || null
    );
  }

  /** Choose Mira's language — switches her text replies AND her voice together. */
  async setVoiceLang(lang: 'en' | 'ta'): Promise<void> {
    if (this.lang() === lang) return;
    await this.i18n.setLanguage(lang);
    // If voice replies are on, confirm in the newly-selected language so the user
    // immediately hears the switch (Tamil voice when 'ta').
    if (this.speakOn()) {
      this.speak(this.t('Voice switched to English.', 'குரல் தமிழுக்கு மாற்றப்பட்டது.'));
    }
  }

  toggleSpeak(): void {
    this.speakOn.update((v) => !v);
    if (this.speakOn()) {
      this.speak(this.t('Voice replies are on.', 'குரல் பதில்கள் இயக்கப்பட்டுள்ளன.'));
    } else {
      this.stopSpeaking();
    }
  }
  private speak(text: string): void {
    if (!this.speakOn() || typeof speechSynthesis === 'undefined') return;
    try {
      speechSynthesis.cancel();
      const u = new SpeechSynthesisUtterance(text.replace(/[•*_#` ]/g, ' ').slice(0, 420));
      const voice = this.pickVoice(this.lang() === 'ta' ? 'ta' : 'en');
      if (voice) u.voice = voice;
      u.lang = this.lang() === 'ta' ? 'ta-IN' : 'en-IN';
      u.rate = this.lang() === 'ta' ? 0.95 : 1.0; // slightly slower for Tamil clarity
      u.pitch = 1.18; // brighter, younger female timbre
      u.onstart = () => this.speaking.set(true);
      u.onend = () => this.speaking.set(false);
      u.onerror = () => this.speaking.set(false);
      speechSynthesis.speak(u);
    } catch { /* ignore */ }
  }
  private stopSpeaking(): void {
    this.speaking.set(false);
    try { if (typeof speechSynthesis !== 'undefined') speechSynthesis.cancel(); } catch { /* ignore */ }
  }

  private scrollToEnd(): void {
    const el = this.scroller()?.nativeElement;
    if (el) el.scrollTop = el.scrollHeight;
  }
  private delay(ms: number): Promise<void> { return new Promise((r) => setTimeout(r, ms)); }

  ngOnDestroy(): void { this.stopListening(); this.stopSpeaking(); }
}
