// ============================================================
// APPLICANT AI — dashboard widget (Layer 5).
// Plain-language status, next steps, reminders + an ask box. Read-only.
// ============================================================
import { Component, ChangeDetectionStrategy, inject, signal, DestroyRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ApplicantAssistantService } from './applicant-assistant.service';

@Component({
  selector: 'app-applicant-ai',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, FormsModule],
  template: `
    <section class="aa" role="region" [attr.aria-label]="t('AI Assistant','AI உதவியாளர்')">
      <header class="aa-head">
        <span class="aa-orb" aria-hidden="true"></span>
        <strong>{{ t('AI Assistant','AI உதவியாளர்') }}</strong>
        @if (app.demo) { <span class="aa-demo">{{ t('sample','மாதிரி') }}</span> }
      </header>

      <p class="aa-explain">{{ assist.explain(app, lang()) }}</p>

      <div class="aa-cols">
        <div class="aa-col">
          <h4>{{ t('Next steps','அடுத்த படிகள்') }}</h4>
          <ul>@for (s of assist.nextSteps(app, lang()); track s) { <li>{{ s }}</li> }</ul>
        </div>
        @if (assist.reminders(app, lang()).length) {
          <div class="aa-col">
            <h4>{{ t('Reminders','நினைவூட்டல்கள்') }}</h4>
            <ul class="aa-rem">@for (r of assist.reminders(app, lang()); track r) { <li>{{ r }}</li> }</ul>
          </div>
        }
      </div>

      <form class="aa-ask" (ngSubmit)="ask()">
        <input [ngModel]="draft()" (ngModelChange)="draft.set($event)" name="q" type="text"
               [placeholder]="t('Ask about your application…','உங்கள் விண்ணப்பம் பற்றி கேளுங்கள்…')"
               [attr.aria-label]="t('Ask the assistant','உதவியாளரிடம் கேள்')" autocomplete="off" />
        <button type="submit" [disabled]="!draft().trim()">{{ t('Ask','கேள்') }}</button>
      </form>
      @if (answer(); as ans) { <p class="aa-answer" role="status">{{ ans }}</p> }
    </section>
  `,
  styles: [`
    .aa { background:#fff; border:1px solid #e2e8f0; border-radius:16px; padding:1.2rem 1.3rem; box-shadow:0 12px 40px -24px rgba(40,20,80,.3); }
    .aa-head { display:flex; align-items:center; gap:.55rem; margin-bottom:.7rem; }
    .aa-head strong { font-size:1.05rem; color:#0f172a; }
    .aa-orb { width:22px; height:22px; border-radius:50%; background:conic-gradient(from 0deg,#ff4fa3,#c084fc,#38bdf8,#22d3ee,#34d399,#a78bfa,#ff4fa3); box-shadow:0 0 10px -2px rgba(139,92,246,.7); animation:aa-spin 5s linear infinite; }
    @keyframes aa-spin { to { transform:rotate(360deg); } }
    .aa-demo { font-size:.68rem; font-weight:600; color:#92400e; background:#fef3c7; border:1px solid #fde68a; padding:1px 7px; border-radius:999px; }
    .aa-explain { margin:0 0 1rem; color:#334155; font-size:.98rem; line-height:1.6; }
    .aa-cols { display:grid; grid-template-columns:repeat(auto-fit,minmax(220px,1fr)); gap:1rem 1.5rem; margin-bottom:1rem; }
    .aa-col h4 { margin:0 0 .4rem; font-size:.82rem; font-weight:700; color:#475569; text-transform:uppercase; letter-spacing:.03em; }
    .aa-col ul { margin:0; padding-left:1.1rem; }
    .aa-col li { color:#334155; font-size:.9rem; line-height:1.55; margin-bottom:.25rem; }
    .aa-rem li { color:#b45309; }
    .aa-ask { display:flex; gap:.5rem; }
    .aa-ask input { flex:1; min-width:0; padding:.6rem .8rem; border:1px solid #e2e8f0; border-radius:10px; font-size:.92rem; outline:none; }
    .aa-ask input:focus { border-color:#7c3aed; }
    .aa-ask button { padding:.6rem 1.1rem; border:none; border-radius:10px; background:#7c3aed; color:#fff; font-weight:600; font-size:.9rem; cursor:pointer; }
    .aa-ask button:disabled { opacity:.5; cursor:not-allowed; }
    .aa-answer { margin:.8rem 0 0; padding:.7rem .85rem; background:#f5f3ff; border:1px solid #ede9fe; border-radius:10px; color:#4c1d95; font-size:.92rem; line-height:1.55; white-space:pre-line; }
    @media (prefers-reduced-motion:reduce){ .aa-orb{ animation:none; } }
  `],
})
export class ApplicantAiComponent {
  readonly assist = inject(ApplicantAssistantService);
  private readonly translate = inject(TranslateService);
  private readonly destroyRef = inject(DestroyRef);

  readonly lang = signal<'en' | 'ta'>((this.translate.currentLang as 'en' | 'ta') || 'en');
  readonly draft = signal('');
  readonly answer = signal<string | null>(null);
  readonly app = this.assist.sample;

  constructor() {
    this.translate.onLangChange.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((e) => {
      this.lang.set((e.lang as 'en' | 'ta') || 'en');
    });
  }

  t(en: string, ta: string): string { return this.lang() === 'ta' ? ta : en; }

  ask(): void {
    const q = this.draft().trim();
    if (!q) return;
    this.answer.set(this.assist.ask(q, this.app, this.lang()));
    this.draft.set('');
  }
}
