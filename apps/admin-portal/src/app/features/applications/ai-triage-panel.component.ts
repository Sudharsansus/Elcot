// ============================================================
// AI TRIAGE PANEL — Layer 7 (officer decision support; read-only).
// ============================================================
import { Component, ChangeDetectionStrategy, inject, signal, DestroyRef, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateService } from '@ngx-translate/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { OfficerTriageService } from './officer-triage.service';

@Component({
  selector: 'app-ai-triage-panel',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule],
  template: `
    <section class="tri" role="region" [attr.aria-label]="t('AI triage','AI முன்வரிசை')">
      <header class="tri-head">
        <span class="tri-orb" aria-hidden="true"></span>
        <div class="tri-titles">
          <strong>{{ t('AI Triage','AI முன்வரிசை') }} · {{ svc.sample.ref }}</strong>
          <span>{{ t('Decision support — the AI never approves or rejects','முடிவு துணை — AI அங்கீகரிக்காது/நிராகரிக்காது') }}</span>
        </div>
        @if (r().demo) { <span class="tri-demo">{{ t('sample','மாதிரி') }}</span> }
      </header>

      <p class="tri-summary">{{ r().summary }}</p>

      <div class="tri-grid">
        <div class="tri-block">
          <h4>{{ t('Risk flags','அபாயக் கொடிகள்') }}</h4>
          <div class="tri-chips">
            @for (rk of r().risks; track rk.label) {
              <span class="tri-chip" [attr.data-level]="rk.level">
                <span class="tri-dot" aria-hidden="true"></span>{{ rk.label }}
              </span>
            }
          </div>
        </div>

        @if (r().duplicates.length) {
          <div class="tri-block">
            <h4>{{ t('Duplicate detection','நகல் கண்டறிதல்') }}</h4>
            <ul class="tri-dupes">@for (d of r().duplicates; track d) { <li>{{ d }}</li> }</ul>
          </div>
        }
      </div>

      <div class="tri-reco">
        <div class="tri-reco-top">
          <h4>{{ t('Recommendation','பரிந்துரை') }}</h4>
          <span class="tri-conf">{{ t('confidence','நம்பிக்கை') }} {{ (r().confidence * 100) | number:'1.0-0' }}%</span>
        </div>
        <p>{{ r().recommendation }}</p>
      </div>

      <p class="tri-foot">{{ t('This is guidance for the reviewing officer. Approval, rejection and sanction remain a human decision.','இது மதிப்பாய்வு அலுவலருக்கான வழிகாட்டுதல். அங்கீகாரம்/நிராகரிப்பு மனித முடிவே.') }}</p>
    </section>
  `,
  styles: [`
    .tri { background:#fff; border:1px solid #e2e8f0; border-radius:16px; padding:1.2rem 1.3rem; box-shadow:0 12px 40px -24px rgba(40,20,80,.3); margin-bottom:1.5rem; }
    .tri-head { display:flex; align-items:center; gap:.6rem; margin-bottom:.8rem; }
    .tri-orb { width:24px; height:24px; border-radius:50%; flex-shrink:0; background:conic-gradient(from 0deg,#ff4fa3,#c084fc,#38bdf8,#22d3ee,#34d399,#a78bfa,#ff4fa3); box-shadow:0 0 10px -2px rgba(139,92,246,.7); animation:tri-spin 5s linear infinite; }
    @keyframes tri-spin { to { transform:rotate(360deg); } }
    .tri-titles { display:flex; flex-direction:column; line-height:1.25; margin-right:auto; }
    .tri-titles strong { font-size:1rem; color:#0f172a; }
    .tri-titles span { font-size:.75rem; color:#64748b; }
    .tri-demo { font-size:.68rem; font-weight:600; color:#92400e; background:#fef3c7; border:1px solid #fde68a; padding:1px 7px; border-radius:999px; }
    .tri-summary { margin:0 0 1rem; color:#334155; font-size:.96rem; line-height:1.6; }
    .tri-grid { display:grid; grid-template-columns:repeat(auto-fit,minmax(240px,1fr)); gap:1rem 1.5rem; margin-bottom:1rem; }
    .tri-block h4 { margin:0 0 .5rem; font-size:.78rem; font-weight:700; color:#475569; text-transform:uppercase; letter-spacing:.03em; }
    .tri-chips { display:flex; flex-wrap:wrap; gap:.4rem; }
    .tri-chip { display:inline-flex; align-items:center; gap:.4rem; font-size:.82rem; padding:.3rem .7rem; border-radius:999px; border:1px solid #e2e8f0; color:#334155; }
    .tri-dot { width:8px; height:8px; border-radius:50%; background:#94a3b8; }
    .tri-chip[data-level="high"] { border-color:#fecaca; background:#fef2f2; color:#991b1b; } .tri-chip[data-level="high"] .tri-dot { background:#dc2626; }
    .tri-chip[data-level="medium"] { border-color:#fde68a; background:#fffbeb; color:#92400e; } .tri-chip[data-level="medium"] .tri-dot { background:#d97706; }
    .tri-chip[data-level="low"] { border-color:#bbf7d0; background:#f0fdf4; color:#166534; } .tri-chip[data-level="low"] .tri-dot { background:#16a34a; }
    .tri-dupes { margin:0; padding-left:1.1rem; } .tri-dupes li { color:#b45309; font-size:.9rem; line-height:1.55; }
    .tri-reco { border:1px solid #ede9fe; background:#f5f3ff; border-radius:12px; padding:.85rem 1rem; }
    .tri-reco-top { display:flex; align-items:center; justify-content:space-between; gap:1rem; }
    .tri-reco h4 { margin:0 0 .3rem; font-size:.82rem; font-weight:700; color:#5b21b6; text-transform:uppercase; letter-spacing:.03em; }
    .tri-conf { font-size:.75rem; font-weight:600; color:#7c3aed; }
    .tri-reco p { margin:.2rem 0 0; color:#4c1d95; font-size:.92rem; line-height:1.55; }
    .tri-foot { margin:.9rem 0 0; font-size:.72rem; color:#94a3b8; }
    @media (prefers-reduced-motion:reduce){ .tri-orb{ animation:none; } }
  `],
})
export class AiTriagePanelComponent {
  readonly svc = inject(OfficerTriageService);
  private readonly translate = inject(TranslateService);
  private readonly destroyRef = inject(DestroyRef);
  private readonly lang = signal<'en' | 'ta'>((this.translate.currentLang as 'en' | 'ta') || 'en');
  readonly r = computed(() => this.svc.triage(this.svc.sample, this.lang()));

  constructor() {
    this.translate.onLangChange.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((e) => {
      this.lang.set((e.lang as 'en' | 'ta') || 'en');
    });
  }

  t(en: string, ta: string): string { return this.lang() === 'ta' ? ta : en; }
}
