// ============================================================
// REVEAL-ON-SCROLL — varied, intentional, GPU-cheap, a11y-safe
// ============================================================
// Adds a one-shot reveal animation when an element scrolls into view, using an
// IntersectionObserver. Fully gated by prefers-reduced-motion and SSR-safe:
// when motion is reduced, the platform is the server, or IntersectionObserver
// is unavailable, content is shown immediately with no transform/transition.
//
// Variants (so reveals are *varied*, not the homogeneous "everything fades up"):
//   up (default) | left | right | scale | fade
// Optional `revealDelay` (ms) staggers siblings.
//
// Visual states live in styles.scss (.reveal / .reveal--* / .is-visible) so the
// animation is pure opacity+transform (compositor-friendly).
// ============================================================

import {
  Directive, ElementRef, Input, OnInit, OnDestroy, inject, PLATFORM_ID, NgZone,
} from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

type RevealVariant = 'up' | 'left' | 'right' | 'scale' | 'fade';

@Directive({
  selector: '[appReveal]',
  standalone: true,
})
export class RevealDirective implements OnInit, OnDestroy {
  @Input('appReveal') variant: RevealVariant | '' = 'up';
  /** Stagger in milliseconds before this element reveals. */
  @Input() revealDelay = 0;

  private readonly host = inject(ElementRef<HTMLElement>);
  private readonly platformId = inject(PLATFORM_ID);
  private readonly zone = inject(NgZone);
  private observer?: IntersectionObserver;

  ngOnInit(): void {
    const el = this.host.nativeElement;

    // SSR or no IO support → just show it.
    if (!isPlatformBrowser(this.platformId) || typeof IntersectionObserver === 'undefined') {
      el.classList.add('reveal', 'is-visible');
      return;
    }

    // Honour the user's reduced-motion preference: no hidden state, no motion.
    const reduce = window.matchMedia?.('(prefers-reduced-motion: reduce)').matches;
    if (reduce) {
      el.classList.add('reveal', 'is-visible');
      return;
    }

    // Already on screen at init (above the fold / hydrated SSR)? Show it
    // immediately — never hide-then-reveal, which would flash after hydration.
    const rect = el.getBoundingClientRect();
    const inView = rect.top < (window.innerHeight || 0) * 0.9 && rect.bottom > 0;
    if (inView) {
      el.classList.add('reveal', 'is-visible');
      return;
    }

    const variant = this.variant || 'up';
    el.classList.add('reveal');
    if (variant !== 'up') el.classList.add(`reveal--${variant}`);

    // Observe outside Angular to avoid needless change-detection churn.
    this.zone.runOutsideAngular(() => {
      this.observer = new IntersectionObserver(
        (entries) => {
          for (const entry of entries) {
            if (!entry.isIntersecting) continue;
            const reveal = () => el.classList.add('is-visible');
            if (this.revealDelay > 0) window.setTimeout(reveal, this.revealDelay);
            else reveal();
            this.observer?.unobserve(el);
          }
        },
        { threshold: 0.12, rootMargin: '0px 0px -8% 0px' },
      );
      this.observer.observe(el);
    });
  }

  ngOnDestroy(): void {
    this.observer?.disconnect();
  }
}
