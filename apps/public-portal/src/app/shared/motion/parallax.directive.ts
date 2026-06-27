// ============================================================
// PARALLAX — GSAP ScrollTrigger scrub. Element drifts vertically as it
// passes through the viewport. Browser-only, reduced-motion safe.
// Usage: <div [appParallax]="120"> (px of total travel)
// ============================================================
import {
  Directive, ElementRef, inject, input, afterNextRender, NgZone, OnDestroy,
} from '@angular/core';
import { gsap } from 'gsap';
import { ScrollTrigger } from 'gsap/ScrollTrigger';

@Directive({ selector: '[appParallax]', standalone: true })
export class ParallaxDirective implements OnDestroy {
  readonly amount = input(80, { alias: 'appParallax' });
  private readonly el = inject<ElementRef<HTMLElement>>(ElementRef);
  private readonly zone = inject(NgZone);
  private tween?: gsap.core.Tween;

  constructor() {
    afterNextRender(() => {
      if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) return;
      this.zone.runOutsideAngular(() => {
        gsap.registerPlugin(ScrollTrigger);
        const d = Number(this.amount()) || 80;
        this.tween = gsap.fromTo(
          this.el.nativeElement,
          { y: -d / 2 },
          {
            y: d / 2,
            ease: 'none',
            scrollTrigger: {
              trigger: this.el.nativeElement,
              start: 'top bottom',
              end: 'bottom top',
              // Numeric scrub adds ~0.6s catch-up smoothing so the transform
              // doesn't jitter on Lenis's sub-pixel interpolated scroll deltas
              // (scrub:true ties it directly to scroll → visible vibration).
              scrub: 0.6,
            },
          },
        );
      });
    });
  }

  ngOnDestroy(): void {
    this.tween?.scrollTrigger?.kill();
    this.tween?.kill();
  }
}
