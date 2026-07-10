// ============================================================
// AI MODE — the portal's primary, agentic interaction surface.
//
// This is the "AI-first" layer: the user states intent (typed or spoken) and
// the platform ACTS — navigates, finds schemes, checks eligibility, pre-fills
// forms, tracks status. It is model-agnostic and LLM-ready:
//   • When the offline, self-hosted model is wired, the backend returns a
//     server-validated `action` (tool + args) on ChatTurnResponse, and this
//     service EXECUTES it. No API keys — data stays on the government server.
//   • Until then, a local intent engine produces the same action shape, so AI
//     Mode is fully demo-able today.
//
// Safety invariants (must hold — they are bid commitments):
//   • Allow-list only (mirrors the server guard); unknown tools are ignored.
//   • navigate stays on INTERNAL routes; fillForm only on known forms.
//   • Any state-changing action (navigate / openSchemeFinder / fillForm) is
//     CONFIRMED first — AI never acts silently.
//   • AI Mode gets NO privilege beyond manual navigation: Angular route guards
//     still apply, and it never touches an approval/decision endpoint.
// ============================================================
import { Injectable, inject, signal, computed } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { Router, NavigationEnd } from '@angular/router';
import { filter, firstValueFrom, map } from 'rxjs';
import { ChatService } from './chat.service';
import { SmoothScrollService } from '../../core/services/smooth-scroll.service';
import { I18nService } from '../../core/services/i18n.service';
import { FormAssistService } from './form-assist.service';
import { SCHEMES_DATA, SECTOR_CATEGORIES, Scheme } from '../schemes/schemes.data';

export type AiTool = 'navigate' | 'openSchemeFinder' | 'fillForm' | 'findScheme' | 'listSchemes';
export interface AiAction { tool: string; args?: Record<string, unknown>; }
export interface PendingAction { action: AiAction; label: string; }
export interface QuickPrompt { en: string; ta: string; q: string; }

@Injectable({ providedIn: 'root' })
export class AiModeService {
  private readonly chat = inject(ChatService);
  private readonly router = inject(Router);
  private readonly smooth = inject(SmoothScrollService);
  private readonly i18n = inject(I18nService);
  private readonly formAssist = inject(FormAssistService);

  /** State-changing tools require an explicit confirmation before running. */
  private static readonly STATE_CHANGING: ReadonlySet<string> = new Set([
    'navigate', 'openSchemeFinder', 'fillForm',
  ]);
  private static readonly VALID_CATEGORIES: ReadonlyArray<string> = [
    'production', 'training', 'infrastructure', 'export', 'freelancer', 'scholarship',
  ];

  // ---- reactive state (shared session with ChatService) ----
  readonly open = signal(false);
  readonly busy = signal(false);
  /** True when the backend AI service is unreachable — the UI degrades to the menu. */
  readonly unavailable = signal(false);
  /** A state-changing action awaiting the user's confirmation, or null. */
  readonly pending = signal<PendingAction | null>(null);
  /** True when the last answer was grounded in official policy docs (RAG). */
  readonly grounded = signal(false);
  readonly messages = this.chat.messages;
  readonly lang = this.i18n.currentLanguage;

  private readonly routeUrl = toSignal(
    this.router.events.pipe(
      filter((e): e is NavigationEnd => e instanceof NavigationEnd),
      map(() => this.router.url),
    ),
    { initialValue: this.router.url },
  );

  /** Context-aware starter prompts for the page the user is on. */
  readonly suggestions = computed<QuickPrompt[]>(() => this.promptsFor(this.routeUrl()));

  private t(en: string, ta: string): string { return this.lang() === 'ta' ? ta : en; }

  // ---- open / close ----
  openMode(): void {
    this.open.set(true);
    if (this.messages().length === 0) this.chat.push('ASSISTANT', this.greeting());
  }
  close(): void { this.open.set(false); this.pending.set(null); }
  toggle(): void { this.open() ? this.close() : this.openMode(); }

  // ---- main entry: the user asked AI Mode to do something ----
  async run(text: string): Promise<void> {
    const q = text.trim();
    if (!q || this.busy()) return;
    this.chat.push('USER', q);
    this.pending.set(null);
    this.grounded.set(false);
    this.busy.set(true);

    // Local intent engine — works offline / against the rule-based backend.
    const local = this.detect(q);
    let reply = local.reply;
    let action: AiAction | null = local.action;

    // Ask the backend (RAG + offline LLM once wired). A substantive answer
    // replaces the local one; a server-validated action takes precedence.
    const r = await firstValueFrom(this.chat.askBackend(q))
      .catch(() => ({ reply: null, action: null as AiAction | null, sources: [] as string[], ok: false }));
    this.unavailable.set(!r.ok); // backend down → degraded mode; local intent still drives
    if (r.ok) {
      const br = (r.reply ?? '').trim();
      const unhelpful =
        !br || br.length < 18 ||
        /can'?t help|cannot help|couldn'?t help|unable to help|i'?m not sure|don'?t know|trouble reaching/i.test(br);
      if (br && !unhelpful) {
        reply = br;
        this.grounded.set((r.sources?.length ?? 0) > 0);
      }
      if (r.action && this.isValid(r.action as AiAction)) action = r.action as AiAction;
    }

    this.busy.set(false);
    this.chat.push('ASSISTANT', reply);

    if (!action) return;
    if (AiModeService.STATE_CHANGING.has(action.tool)) {
      // Never auto-execute: render a one-line confirmation.
      this.pending.set({ action, label: this.confirmLabel(action) });
    } else {
      // Read-only (findScheme / listSchemes) may render immediately.
      this.dispatch(action);
    }
  }

  /** User approved the pending state-changing action. */
  confirm(): void {
    const p = this.pending();
    if (!p) return;
    this.pending.set(null);
    this.dispatch(p.action);
  }

  /** User declined the pending action. */
  cancel(): void {
    if (!this.pending()) return;
    this.pending.set(null);
    this.chat.push('ASSISTANT', this.t(
      'Okay, cancelled. What else can I do for you?',
      'சரி, ரத்து செய்யப்பட்டது. வேறு என்ன செய்யலாம்?'));
  }

  /**
   * Validated executor. Runs ONLY allow-listed tools; navigate stays internal;
   * fillForm only opens known forms. Anything else is ignored.
   */
  dispatch(action: AiAction): void {
    if (!this.isValid(action)) return;
    const args = action.args ?? {};
    switch (action.tool) {
      case 'navigate': {
        const route = String(args['route'] ?? '');
        void this.router.navigateByUrl(route); // route guards still apply — no elevated privilege
        setTimeout(() => this.close(), 500);
        break;
      }
      case 'openSchemeFinder': {
        const go = () => setTimeout(() => this.smooth.scrollTo('#scheme-finder', -90), 450);
        if (this.router.url.split('?')[0] !== '/') void this.router.navigateByUrl('/').then(go);
        else go();
        setTimeout(() => this.close(), 600);
        break;
      }
      case 'fillForm': {
        const route = String(args['form'] ?? '') === 'contact' ? '/contact' : '/auth/register';
        const begin = () => setTimeout(() => this.formAssist.begin(), 500);
        if (this.router.url.split('?')[0] !== route) void this.router.navigateByUrl(route).then(begin);
        else begin();
        setTimeout(() => this.close(), 500);
        break;
      }
      case 'findScheme': {
        const c = String(args['category'] ?? '') as Scheme['category'];
        const valid = AiModeService.VALID_CATEGORIES.includes(c);
        this.chat.push('ASSISTANT', this.schemeList(valid ? c : undefined));
        break;
      }
      case 'listSchemes': {
        this.chat.push('ASSISTANT', this.schemeList());
        break;
      }
    }
  }

  /** Allow-list + per-tool validation — mirrors the server-side ChatService guard. */
  isValid(action: AiAction | null | undefined): boolean {
    if (!action) return false;
    const args = action.args ?? {};
    switch (action.tool) {
      case 'navigate': return String(args['route'] ?? '').startsWith('/');
      case 'fillForm': {
        const f = String(args['form'] ?? '');
        return f === 'register' || f === 'contact';
      }
      case 'openSchemeFinder':
      case 'findScheme':
      case 'listSchemes':
        return true;
      default:
        return false; // unknown tool — ignore
    }
  }

  /** Human-readable one-line confirmation for a state-changing action. */
  private confirmLabel(action: AiAction): string {
    const args = action.args ?? {};
    switch (action.tool) {
      case 'navigate':
        return this.t(
          `I'll take you to ${this.routeLabel(String(args['route'] ?? ''))} — proceed?`,
          `${this.routeLabel(String(args['route'] ?? ''))} பக்கத்திற்கு அழைத்துச் செல்லவா? — தொடரவா?`);
      case 'openSchemeFinder':
        return this.t(
          "I'll open the Scheme Finder so we can match you to eligible incentives — proceed?",
          'தகுதியான திட்டங்களைப் பொருத்த Scheme Finder-ஐத் திறக்கவா? — தொடரவா?');
      case 'fillForm':
        return String(args['form'] ?? '') === 'contact'
          ? this.t("I'll open the contact form and help you fill it — proceed?",
                   'தொடர்பு படிவத்தைத் திறந்து நிரப்ப உதவவா? — தொடரவா?')
          : this.t("I'll open the registration form and help you fill it — proceed?",
                   'பதிவு படிவத்தைத் திறந்து நிரப்ப உதவவா? — தொடரவா?');
      default:
        return this.t('Proceed?', 'தொடரவா?');
    }
  }

  private routeLabel(route: string): string {
    const map: Record<string, [string, string]> = {
      '/schemes': ['the Schemes catalogue', 'திட்டப் பட்டியல்'],
      '/companies': ['Business Connect', 'பிசினஸ் கனெக்ட்'],
      '/talent': ['Talent Connect', 'டேலன்ட் கனெக்ட்'],
      '/freelancers': ['the Freelancer Registry', 'ஃப்ரீலான்சர் பதிவு'],
      '/events': ['News & Events', 'செய்திகள் & நிகழ்வுகள்'],
      '/resources': ['Resources', 'வளங்கள்'],
      '/about': ['About', 'பற்றி'],
      '/contact': ['Contact', 'தொடர்பு'],
      '/auth/register': ['Registration', 'பதிவு'],
      '/auth/login': ['Sign in', 'உள்நுழைவு'],
    };
    const key = Object.keys(map).find((k) => route.split('?')[0] === k || route.split('?')[0].startsWith(k + '/'));
    const pick = key ? map[key] : ['that page', 'அந்தப் பக்கம்'];
    return this.lang() === 'ta' ? pick[1] : pick[0];
  }

  // ============================================================
  // Local intent engine — maps a natural-language request to {reply, action}.
  // Kept deliberately conservative; the offline model will supersede it with
  // richer understanding via the same action protocol.
  // ============================================================
  private detect(raw: string): { reply: string; action: AiAction | null } {
    const x = ` ${raw.toLowerCase()} `;
    const has = (...k: string[]) => k.some((s) => x.includes(s));

    const sector = SECTOR_CATEGORIES.find(
      (s) => x.includes(s.labelEn.toLowerCase()) || x.includes(s.key.toLowerCase()) || raw.includes(s.labelTa),
    );
    if (sector && has('scheme', 'incentive', 'grant', 'subsid', 'fund', 'support')) {
      return {
        reply: this.t(
          `Here are the incentives relevant to ${sector.labelEn}. The Schemes catalogue lets you filter by category and funding.`,
          `${sector.labelTa} தொடர்பான ஊக்கத்திட்டங்கள் இதோ. வகை மற்றும் நிதி அடிப்படையில் வடிகட்டலாம்.`),
        action: { tool: 'navigate', args: { route: '/schemes' } },
      };
    }
    if (has('find', 'eligib', 'which scheme', 'suitable', 'match', 'recommend', 'for me', 'qualify')) {
      return {
        reply: this.t(
          "Let's match you to the right scheme. The Scheme Finder asks your profile (studio, individual or student), sector and stage, then filters the incentives you qualify for.",
          'சரியான திட்டத்தைப் பொருத்துவோம். Scheme Finder உங்கள் சுயவிவரம், துறை, நிலையைக் கேட்டு தகுதியானவற்றை வடிகட்டும்.'),
        action: { tool: 'openSchemeFinder' },
      };
    }

    // category keyword → scheme bucket (for filtered lists)
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
      return {
        reply: cat
          ? this.t('Matching schemes:', 'பொருந்தும் திட்டங்கள்:')
          : this.t(`There are ${SCHEMES_DATA.length} active incentive schemes:`, `${SCHEMES_DATA.length} செயலில் உள்ள திட்டங்கள்:`),
        action: cat ? { tool: 'findScheme', args: { category: cat } } : { tool: 'listSchemes' },
      };
    }
    if (has('deadline', 'last date', 'due date', 'closing date', 'when to apply', 'when does it close', 'application date')) {
      const lines = SCHEMES_DATA.map((s) => `• ${this.lang() === 'ta' ? s.nameTa : s.name}: ${s.deadline}`).join('\n');
      return { reply: this.t(`Application deadlines:\n${lines}`, `விண்ணப்ப காலக்கெடு:\n${lines}`), action: null };
    }
    if (has('document', 'what do i need', 'papers', 'what to submit', 'certificate')) {
      return {
        reply: this.t(
          'Typical documents: company registration, GST & PAN, a project proposal, budget, audited financials and a portfolio. Each scheme lists its exact set in the catalogue.',
          'பொதுவான ஆவணங்கள்: நிறுவன பதிவு, GST & PAN, திட்ட முன்மொழிவு, பட்ஜெட், தணிக்கை அறிக்கை, போர்ட்ஃபோலியோ.'),
        action: { tool: 'navigate', args: { route: '/schemes' } },
      };
    }
    if (has('status', 'track', 'my application', 'application status', 'where is my', 'check my')) {
      return {
        reply: this.t(
          'Application status lives on your dashboard once you sign in.',
          'உள்நுழைந்ததும் உங்கள் டாஷ்போர்டில் விண்ணப்ப நிலை தெரியும்.'),
        action: { tool: 'navigate', args: { route: '/auth/login' } },
      };
    }
    if (has('fill', 'help me with the form', 'fill in', 'fill out', 'complete the form')) {
      const form = has('contact', 'grievance', 'complaint') ? 'contact' : 'register';
      return {
        reply: this.t(
          "I can fill the form with you — one field at a time, checking each as we go.",
          'படிவத்தை ஒன்றாக நிரப்பலாம் — ஒவ்வொன்றாகச் சரிபார்த்து.'),
        action: { tool: 'fillForm', args: { form } },
      };
    }
    if (has('scheme', 'incentive', 'grant', 'subsid', 'funding', 'financial')) {
      if (cat) {
        return { reply: this.t('Matching schemes:', 'பொருந்தும் திட்டங்கள்:'), action: { tool: 'findScheme', args: { category: cat } } };
      }
      return {
        reply: this.t(
          `There are ${SCHEMES_DATA.length} active incentive schemes — production subsidy, training, XR infrastructure, market access, freelancer support and scholarships.`,
          `${SCHEMES_DATA.length} செயலில் உள்ள ஊக்கத்திட்டங்கள் — உற்பத்தி மானியம், பயிற்சி, XR உள்கட்டமைப்பு, சந்தை அணுகல், ஃப்ரீலான்சர் ஆதரவு, உதவித்தொகை.`),
        action: { tool: 'navigate', args: { route: '/schemes' } },
      };
    }
    if (has('apply', 'how do i', 'register', 'sign up', 'signup', 'enrol', 'create account')) {
      return {
        reply: this.t(
          'Applying is fully online in 5 steps: register, check eligibility, submit your form & documents, departmental review, then sanction & disbursal.',
          'விண்ணப்பம் முழுவதும் ஆன்லைனில் — 5 படிகள்: பதிவு, தகுதி, சமர்ப்பணம், மதிப்பாய்வு, அனுமதி & வழங்கல்.'),
        action: { tool: 'navigate', args: { route: '/auth/register' } },
      };
    }
    if (has('compan', 'studio', 'business connect')) {
      return {
        reply: this.t('Business Connect is the directory of AVGC-XR companies and studios across Tamil Nadu.', 'Business Connect — தமிழ்நாட்டின் AVGC-XR நிறுவனங்களின் அடைவு.'),
        action: { tool: 'navigate', args: { route: '/companies' } },
      };
    }
    if (has('talent', ' job', 'intern', 'career', 'hire', 'recruit')) {
      return {
        reply: this.t('Talent Connect is the skill registry of students and professionals.', 'Talent Connect — மாணவர்கள் & நிபுணர்களின் திறன் பதிவு.'),
        action: { tool: 'navigate', args: { route: '/talent' } },
      };
    }
    if (has('freelanc')) {
      return {
        reply: this.t('The Freelancer Registry lists independent AVGC-XR professionals.', 'Freelancer Registry — சுயாதீன AVGC-XR நிபுணர்கள்.'),
        action: { tool: 'navigate', args: { route: '/freelancers' } },
      };
    }
    if (has('event', 'news', 'workshop', 'webinar')) {
      return { reply: this.t('News & Events lists the latest updates, workshops and webinars.', 'செய்திகள் & நிகழ்வுகள் — சமீபத்திய புதுப்பிப்புகள்.'), action: { tool: 'navigate', args: { route: '/events' } } };
    }
    if (has('resource', 'download', 'guideline', 'policy')) {
      return { reply: this.t('Resources holds policy documents, guidelines and downloads.', 'வளங்கள் — கொள்கை ஆவணங்கள், வழிகாட்டுதல்கள், பதிவிறக்கங்கள்.'), action: { tool: 'navigate', args: { route: '/resources' } } };
    }
    if (has('contact', 'reach', 'phone', ' call', 'email', 'address', 'helpline')) {
      return {
        reply: this.t('You can reach ELCOT at 044 2254 0100 or info@elcot.in.', 'ELCOT: 044 2254 0100 / info@elcot.in.'),
        action: { tool: 'navigate', args: { route: '/contact' } },
      };
    }
    if (has('about', 'what is this', 'who runs', 'who is behind', 'elcot', 'purpose')) {
      return {
        reply: this.t(
          'This is the official Government of Tamil Nadu portal for the AVGC-XR sector — Animation, VFX, Gaming, Comics and Extended Reality — run by ELCOT.',
          'இது தமிழ்நாடு அரசின் அதிகாரப்பூர்வ AVGC-XR இணையதளம், ELCOT-ஆல் நிர்வகிக்கப்படுகிறது.'),
        action: null,
      };
    }
    if (has('hello', ' hi ', ' hey', 'vanakkam', 'வணக்கம்', 'help', 'what can you')) {
      return { reply: this.greeting(), action: null };
    }
    return {
      reply: this.t(
        "I can find schemes, check eligibility, help you apply or fill a form, and take you anywhere on the portal. Try “find me a scheme” or “help me apply”.",
        'திட்டங்கள், தகுதி, விண்ணப்பம், படிவம் நிரப்புதல் — எதற்கும் உதவ முடியும். "எனக்கேற்ற திட்டம்" எனச் சொல்லுங்கள்.'),
      action: null,
    };
  }

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

  private greeting(): string {
    return this.t(
      "Hi, I'm your AI guide to the Tamil Nadu AVGC-XR portal. Tell me what you'd like to do — find a scheme, check eligibility, apply, or jump anywhere — and I'll do it for you.",
      'வணக்கம், நான் தமிழ்நாடு AVGC-XR இணையதளத்தின் AI வழிகாட்டி. என்ன செய்ய வேண்டும் எனச் சொல்லுங்கள் — திட்டம், தகுதி, விண்ணப்பம் — நான் செய்கிறேன்.');
  }

  private promptsFor(url: string): QuickPrompt[] {
    const u = (url.split('?')[0] || '/').toLowerCase();
    const findScheme: QuickPrompt = { en: 'Find me a scheme', ta: 'எனக்கேற்ற திட்டம்', q: 'find me a scheme' };
    if (u.startsWith('/schemes')) return [
      { en: 'Check my eligibility', ta: 'என் தகுதியைச் சரிபார்', q: 'check my eligibility' },
      { en: 'Required documents', ta: 'தேவையான ஆவணங்கள்', q: 'what documents do i need' },
      { en: 'Application deadlines', ta: 'விண்ணப்ப காலக்கெடு', q: 'deadlines' },
      { en: 'Help me apply', ta: 'விண்ணப்பிக்க உதவு', q: 'help me apply' },
      { en: 'List all schemes', ta: 'அனைத்து திட்டங்கள்', q: 'list all schemes' },
    ];
    if (u.startsWith('/companies')) return [
      { en: 'Register my company', ta: 'நிறுவனத்தைப் பதிவு செய்', q: 'help me register' },
      findScheme,
      { en: 'Open Talent Connect', ta: 'டேலன்ட் கனெக்ட்', q: 'open talent connect' },
    ];
    if (u.startsWith('/talent') || u.startsWith('/freelancers')) return [
      { en: 'Register my profile', ta: 'என் சுயவிவரத்தைப் பதிவு செய்', q: 'help me register' },
      findScheme,
      { en: 'Find opportunities', ta: 'வாய்ப்புகளைக் கண்டறி', q: 'open talent connect' },
    ];
    if (u.startsWith('/auth/register')) return [
      { en: 'Fill this form for me', ta: 'இந்தப் படிவத்தை நிரப்பு', q: 'fill the form' },
      { en: 'What documents do I need?', ta: 'தேவையான ஆவணங்கள்?', q: 'what documents do i need' },
    ];
    if (u.startsWith('/contact') || u.startsWith('/grievance')) return [
      { en: 'Fill this form for me', ta: 'இந்தப் படிவத்தை நிரப்பு', q: 'fill the contact form' },
      { en: 'Helpline number', ta: 'உதவி எண்', q: 'contact' },
      findScheme,
    ];
    // default — home and everything else
    return [
      findScheme,
      { en: 'Check my eligibility', ta: 'என் தகுதியைச் சரிபார்', q: 'check my eligibility' },
      { en: 'Help me apply', ta: 'விண்ணப்பிக்க உதவு', q: 'help me apply' },
      { en: 'List all schemes', ta: 'அனைத்து திட்டங்கள்', q: 'list all schemes' },
      { en: 'Application deadlines', ta: 'விண்ணப்ப காலக்கெடு', q: 'deadlines' },
      { en: 'Talent Connect', ta: 'டேலன்ட் கனெக்ட்', q: 'open talent connect' },
    ];
  }
}
