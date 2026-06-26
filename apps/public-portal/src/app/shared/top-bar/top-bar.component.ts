// ============================================================
// TOP UTILITY BAR — ELCOT-style deep-purple strip:
// Government of Tamil Nadu identity (left) + live date, accessibility,
// and social links (right). Sits above the floating header.
// ============================================================
import { Component, ChangeDetectionStrategy, signal, afterNextRender, OnDestroy } from '@angular/core';

@Component({
  selector: 'app-top-bar',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="topbar">
      <div class="container topbar-inner">
        <a class="tb-gov" href="https://www.tn.gov.in" target="_blank" rel="noopener">
          <img src="assets/brand/tn-emblem.png" alt="" width="24" height="24" />
          <span class="tb-gov-text">தமிழ்நாடு அரசு · Government of Tamil Nadu</span>
        </a>

        <div class="tb-right">
          <span class="tb-date" aria-label="Current date and time">{{ now() }}</span>
          <span class="tb-sep" aria-hidden="true"></span>
          <a href="#main-content" class="tb-link">Accessibility</a>
          <span class="tb-follow">Follow Us</span>
          <nav class="tb-social" aria-label="Social links">
            <a href="https://www.facebook.com/profile.php?id=100069319955202" target="_blank" rel="noopener" aria-label="ELCOT on Facebook">
              <svg viewBox="0 0 24 24" width="14" height="14" fill="currentColor"><path d="M13 22v-8h2.7l.4-3H13V9c0-.9.3-1.5 1.6-1.5H16V4.9c-.3 0-1.3-.1-2.4-.1-2.4 0-4 1.5-4 4.1V11H7v3h2.6v8H13z"/></svg>
            </a>
            <a href="https://www.linkedin.com/company/elcot/" target="_blank" rel="noopener" aria-label="ELCOT on LinkedIn">
              <svg viewBox="0 0 24 24" width="14" height="14" fill="currentColor"><path d="M4.98 3.5a2.5 2.5 0 100 5 2.5 2.5 0 000-5zM3 9h4v12H3zM9 9h3.8v1.6h.1c.5-1 1.8-2 3.7-2 4 0 4.7 2.6 4.7 6V21H21v-5.3c0-1.3 0-2.9-1.8-2.9s-2 1.4-2 2.8V21H13z"/></svg>
            </a>
            <a href="https://x.com/ELCOT_TN" target="_blank" rel="noopener" aria-label="ELCOT on X">
              <svg viewBox="0 0 24 24" width="13" height="13" fill="currentColor"><path d="M18.2 2H21l-6.5 7.4L22 22h-6l-4.7-6.1L5.9 22H3l7-8L2 2h6.1l4.2 5.6L18.2 2zm-1 18h1.6L7.9 3.7H6.1L17.2 20z"/></svg>
            </a>
            <a href="https://www.instagram.com/elcot_tn" target="_blank" rel="noopener" aria-label="ELCOT on Instagram">
              <svg viewBox="0 0 24 24" width="14" height="14" fill="currentColor"><path d="M12 2.2c3.2 0 3.6 0 4.9.1 1.2.1 1.8.3 2.2.4.6.2 1 .5 1.4.9.4.4.7.8.9 1.4.1.4.3 1 .4 2.2.1 1.3.1 1.7.1 4.9s0 3.6-.1 4.9c-.1 1.2-.3 1.8-.4 2.2-.2.6-.5 1-.9 1.4-.4.4-.8.7-1.4.9-.4.1-1 .3-2.2.4-1.3.1-1.7.1-4.9.1s-3.6 0-4.9-.1c-1.2-.1-1.8-.3-2.2-.4-.6-.2-1-.5-1.4-.9-.4-.4-.7-.8-.9-1.4-.1-.4-.3-1-.4-2.2C2.2 15.6 2.2 15.2 2.2 12s0-3.6.1-4.9c.1-1.2.3-1.8.4-2.2.2-.6.5-1 .9-1.4.4-.4.8-.7 1.4-.9.4-.1 1-.3 2.2-.4C8.4 2.2 8.8 2.2 12 2.2zm0 1.8c-3.1 0-3.5 0-4.7.1-1.1.1-1.7.2-2.1.4-.5.2-.9.4-1.3.8-.4.4-.6.8-.8 1.3-.2.4-.3 1-.4 2.1C2.6 9.5 2.6 9.9 2.6 12s0 2.5.1 3.7c.1 1.1.2 1.7.4 2.1.2.5.4.9.8 1.3.4.4.8.6 1.3.8.4.2 1 .3 2.1.4 1.2.1 1.6.1 4.7.1s3.5 0 4.7-.1c1.1-.1 1.7-.2 2.1-.4.5-.2.9-.4 1.3-.8.4-.4.6-.8.8-1.3.2-.4.3-1 .4-2.1.1-1.2.1-1.6.1-3.7s0-2.5-.1-3.7c-.1-1.1-.2-1.7-.4-2.1-.2-.5-.4-.9-.8-1.3-.4-.4-.8-.6-1.3-.8-.4-.2-1-.3-2.1-.4-1.2-.1-1.6-.1-4.7-.1zm0 3.1a4.9 4.9 0 110 9.8 4.9 4.9 0 010-9.8zm0 1.8a3.1 3.1 0 100 6.2 3.1 3.1 0 000-6.2zm5.1-3.3a1.15 1.15 0 110 2.3 1.15 1.15 0 010-2.3z"/></svg>
            </a>
          </nav>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .topbar {
      background: var(--purple-deep, #45125c);
      color: rgba(255, 255, 255, 0.92);
      font-size: 0.78rem;
      position: relative; z-index: 1;
    }
    .topbar-inner { display: flex; align-items: center; justify-content: space-between; gap: 1rem; min-height: 38px; padding: 0.3rem var(--space-6, 1.5rem); }
    .tb-gov { display: inline-flex; align-items: center; gap: 0.5rem; color: #fff; }
    .tb-gov img { border-radius: 50%; background: #fff; padding: 1px; }
    .tb-gov-text { font-weight: 500; letter-spacing: 0.01em; }
    .tb-right { display: inline-flex; align-items: center; gap: 0.9rem; }
    .tb-date { font-variant-numeric: tabular-nums; color: rgba(255,255,255,0.85); }
    .tb-sep { width: 1px; height: 14px; background: rgba(255,255,255,0.28); }
    .tb-link { color: #fff; text-decoration: underline; text-underline-offset: 3px; }
    .tb-link:hover { color: var(--violet-light, #d6bcff); }
    .tb-follow { color: rgba(255,255,255,0.75); }
    .tb-social { display: inline-flex; align-items: center; gap: 0.55rem; }
    .tb-social a { color: rgba(255,255,255,0.9); display: grid; place-items: center; transition: color .15s, transform .15s; }
    .tb-social a:hover { color: #fff; transform: translateY(-1px); }
    @media (max-width: 860px) {
      .tb-gov-text { display: none; }
      .tb-date, .tb-follow { display: none; }
    }
  `],
})
export class TopBarComponent implements OnDestroy {
  readonly now = signal('');
  private timer: ReturnType<typeof setInterval> | undefined;

  constructor() {
    afterNextRender(() => {
      const tick = () => this.now.set(
        new Date().toLocaleString('en-GB', {
          weekday: 'short', day: '2-digit', month: 'short', year: 'numeric',
          hour: '2-digit', minute: '2-digit', hour12: true,
        }),
      );
      tick();
      this.timer = setInterval(tick, 30000);
    });
  }

  ngOnDestroy(): void { clearInterval(this.timer); }
}
