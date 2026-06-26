// ============================================================
// TYPEWRITER — types a phrase, holds, backspaces, types the next.
// Cycles bilingual phrases (Tamil first, then English). Browser-only,
// reduced-motion safe. Caret is crimson so it shows through gradient text.
// ============================================================
import {
  Component, ChangeDetectionStrategy, input, signal, afterNextRender, OnDestroy,
} from '@angular/core';

@Component({
  selector: 'app-typewriter',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<span class="tw-text">{{ display() }}</span><span class="tw-caret" aria-hidden="true"></span>`,
  styles: [`
    :host { display: inline; }
    .tw-caret {
      display: inline-block; width: 0.055em; height: 0.95em;
      margin-left: 0.05em; vertical-align: -0.06em;
      background: var(--crimson, #c8102e); border-radius: 2px;
      animation: tw-blink 1.05s steps(1) infinite;
      -webkit-text-fill-color: var(--crimson, #c8102e);
    }
    @keyframes tw-blink { 50% { opacity: 0; } }
    @media (prefers-reduced-motion: reduce) { .tw-caret { animation: none; } }
  `],
})
export class TypewriterComponent implements OnDestroy {
  readonly phrases = input<string[]>([]);
  readonly typeSpeed = input(65);
  readonly backSpeed = input(32);
  readonly holdTime = input(1600);
  readonly startDelay = input(450);

  readonly display = signal('');
  private timer: ReturnType<typeof setTimeout> | undefined;

  constructor() {
    afterNextRender(() => {
      const list = this.phrases();
      if (!list.length) return;
      if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) {
        this.display.set(list[0]);
        return;
      }
      this.timer = setTimeout(() => this.loop(list, 0, 0, false), this.startDelay());
    });
  }

  private loop(list: string[], pi: number, ci: number, deleting: boolean): void {
    const word = list[pi];
    const nextCi = ci + (deleting ? -1 : 1);
    this.display.set(word.slice(0, nextCi));

    let delay = deleting ? this.backSpeed() : this.typeSpeed();
    let nd = deleting, np = pi, nc = nextCi;

    if (!deleting && nextCi === word.length) { nd = true; delay = this.holdTime(); }
    else if (deleting && nextCi === 0) { nd = false; np = (pi + 1) % list.length; delay = 360; }

    this.timer = setTimeout(() => this.loop(list, np, nc, nd), delay);
  }

  ngOnDestroy(): void { clearTimeout(this.timer); }
}
