// ============================================================
// COUNT-UP — animate a number when it scrolls into view
// ============================================================
// Counts from 0 to `appCountUp` with an ease-out curve on first view. Honours
// prefers-reduced-motion and SSR (sets the final value instantly in both
// cases). Uses Indian-locale grouping (en-IN), with optional prefix/suffix and
// decimal precision so it works for "06", "₹500 Cr", "₹1 Cr", etc.
// ============================================================

import {
  Directive, ElementRef, Input, OnInit, OnDestroy, inject, PLATFORM_ID, NgZone, Renderer2,
} from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Directive({
  selector: '[appCountUp]',
  standalone: true,
})
export class CountUpDirective implements OnInit, OnDestroy {
  /** Target value to count up to. */
  @Input('appCountUp') target = 0;
  @Input() countUpDuration = 1600;
  @Input() countUpDecimals = 0;
  @Input() countUpPrefix = '';
  @Input() countUpSuffix = '';
  /** Zero-pad the integer part to this width (e.g. 2 → "06"). 0 disables. */
  @Input() countUpPad = 0;

  private readonly host = inject(ElementRef<HTMLElement>);
  private readonly platformId = inject(PLATFORM_ID);
  private readonly zone = inject(NgZone);
  private readonly renderer = inject(Renderer2);
  private observer?: IntersectionObserver;
  private rafId = 0;

  ngOnInit(): void {
    if (!isPlatformBrowser(this.platformId) || typeof IntersectionObserver === 'undefined') {
      this.render(this.target);
      return;
    }
    const reduce = window.matchMedia?.('(prefers-reduced-motion: reduce)').matches;
    if (reduce) {
      this.render(this.target);
      return;
    }

    // Already visible at init (above the fold / hydrated SSR)? Render the final
    // value immediately — no 0→N reset flash after hydration.
    const rect = this.host.nativeElement.getBoundingClientRect();
    const inView = rect.top < (window.innerHeight || 0) * 0.9 && rect.bottom > 0;
    if (inView) {
      this.render(this.target);
      return;
    }

    this.render(0);
    this.zone.runOutsideAngular(() => {
      this.observer = new IntersectionObserver(
        (entries) => {
          for (const entry of entries) {
            if (!entry.isIntersecting) continue;
            this.animate();
            this.observer?.unobserve(this.host.nativeElement);
          }
        },
        { threshold: 0.4 },
      );
      this.observer.observe(this.host.nativeElement);
    });
  }

  private animate(): void {
    const start = performance.now();
    const from = 0;
    const to = this.target;
    const easeOut = (t: number) => 1 - Math.pow(1 - t, 3);

    const tick = (now: number) => {
      const progress = Math.min((now - start) / this.countUpDuration, 1);
      this.render(from + (to - from) * easeOut(progress));
      if (progress < 1) {
        this.rafId = requestAnimationFrame(tick);
      } else {
        this.render(to);
      }
    };
    this.rafId = requestAnimationFrame(tick);
  }

  private render(value: number): void {
    const rounded = this.countUpDecimals > 0 ? value : Math.round(value);
    let body = rounded.toLocaleString('en-IN', {
      minimumFractionDigits: this.countUpDecimals,
      maximumFractionDigits: this.countUpDecimals,
    });
    if (this.countUpPad > 0 && this.countUpDecimals === 0) {
      body = String(Math.round(value)).padStart(this.countUpPad, '0');
    }
    this.renderer.setProperty(
      this.host.nativeElement,
      'textContent',
      `${this.countUpPrefix}${body}${this.countUpSuffix}`,
    );
  }

  ngOnDestroy(): void {
    this.observer?.disconnect();
    if (this.rafId) cancelAnimationFrame(this.rafId);
  }
}
