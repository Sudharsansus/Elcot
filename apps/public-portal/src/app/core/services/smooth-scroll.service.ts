// ============================================================
// Lenis smooth-scroll — browser-only, reduced-motion aware, zoneless RAF.
// Drives the cinematic inertia scroll the whole site rides on.
// ============================================================
import { Injectable, inject, PLATFORM_ID, NgZone } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import Lenis from 'lenis';

@Injectable({ providedIn: 'root' })
export class SmoothScrollService {
  private readonly platformId = inject(PLATFORM_ID);
  private readonly zone = inject(NgZone);
  private lenis: Lenis | null = null;
  private rafId = 0;

  /** Initialise once (no-op on server or when the user prefers reduced motion). */
  init(): void {
    if (!isPlatformBrowser(this.platformId) || this.lenis) return;
    if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) return;

    this.zone.runOutsideAngular(() => {
      this.lenis = new Lenis({
        duration: 1.15,
        easing: (t: number) => Math.min(1, 1.001 - Math.pow(2, -10 * t)),
        smoothWheel: true,
        lerp: 0.1,
        wheelMultiplier: 1,
        touchMultiplier: 1.6,
      });
      const raf = (time: number) => {
        this.lenis?.raf(time);
        this.rafId = requestAnimationFrame(raf);
      };
      this.rafId = requestAnimationFrame(raf);
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
    cancelAnimationFrame(this.rafId);
    this.lenis?.destroy();
    this.lenis = null;
  }
}
