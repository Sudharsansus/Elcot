// ============================================================
// Lenis smooth-scroll — browser-only, reduced-motion aware, zoneless RAF.
// Drives the cinematic inertia scroll the whole site rides on.
// ============================================================
import { Injectable, inject, PLATFORM_ID, NgZone } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import Lenis from 'lenis';
import { gsap } from 'gsap';
import { ScrollTrigger } from 'gsap/ScrollTrigger';

@Injectable({ providedIn: 'root' })
export class SmoothScrollService {
  private readonly platformId = inject(PLATFORM_ID);
  private readonly zone = inject(NgZone);
  private lenis: Lenis | null = null;
  private ticker?: (time: number) => void;

  /** Lenis JS smooth-scroll is disabled: its per-frame transform of the page
   *  caused sub-pixel shimmer ("vibration") and scroll lag on many devices.
   *  Native scrolling is used instead (rock-solid, zero overhead); GSAP
   *  ScrollTrigger animations work fine on native scroll. Flip to `true` to
   *  bring back the Lenis cinematic inertia. */
  private readonly enabled = false;

  /** Initialise once (no-op on server, when disabled, or under reduced motion). */
  init(): void {
    if (!isPlatformBrowser(this.platformId) || this.lenis) return;
    if (!this.enabled) return;
    if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) return;

    this.zone.runOutsideAngular(() => {
      gsap.registerPlugin(ScrollTrigger);
      this.lenis = new Lenis({
        duration: 1.15,
        easing: (t: number) => Math.min(1, 1.001 - Math.pow(2, -10 * t)),
        smoothWheel: true,
        lerp: 0.1,
        wheelMultiplier: 1,
        touchMultiplier: 1.6,
      });
      this.lenis.on('scroll', ScrollTrigger.update);
      this.ticker = (time: number) => this.lenis?.raf(time * 1000);
      gsap.ticker.add(this.ticker);
      gsap.ticker.lagSmoothing(0);
    });
  }

  scrollTo(target: string | number | HTMLElement, offset = 0): void {
    if (this.lenis) {
      this.lenis.scrollTo(target as never, { offset });
    } else if (isPlatformBrowser(this.platformId) && typeof target === 'string') {
      document.querySelector(target)?.scrollIntoView({ behavior: 'smooth' });
    }
  }

  stop(): void { this.lenis?.stop(); }
  start(): void { this.lenis?.start(); }

  destroy(): void {
    if (this.ticker) gsap.ticker.remove(this.ticker);
    ScrollTrigger.getAll().forEach((t) => t.kill());
    this.lenis?.destroy();
    this.lenis = null;
  }
}
