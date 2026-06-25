// ============================================================
// HERO XR CANVAS — decorative, perf-budgeted constellation field
// ============================================================
// A lightweight Canvas-2D particle/line field that gives the masthead an
// "XR / motion-native" feel without a 3D dependency or bundle cost. It is:
//   • decorative — aria-hidden, never announced, never focusable;
//   • reduced-motion aware — renders ONE static frame, no animation loop;
//   • cheap — DPR capped at 2, particle count scales with area (hard cap),
//     drawn outside Angular, paused when the tab is hidden or it scrolls
//     offscreen (IntersectionObserver), so it never burns the battery idle;
//   • SSR-safe — nothing runs on the server.
// ============================================================

import {
  Component, ChangeDetectionStrategy, ElementRef, Input, AfterViewInit, OnDestroy,
  inject, PLATFORM_ID, NgZone, viewChild,
} from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

interface Particle { x: number; y: number; vx: number; vy: number; r: number; a: number; }

@Component({
  selector: 'app-hero-canvas',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<canvas #cv aria-hidden="true" class="hero-canvas"></canvas>`,
  styles: [`
    :host { position: absolute; inset: 0; display: block; pointer-events: none; }
    .hero-canvas { width: 100%; height: 100%; display: block; }
  `],
})
export class HeroCanvasComponent implements AfterViewInit, OnDestroy {
  /** Dot colour (rgb triplet, e.g. "255,255,255"). */
  @Input() dot = '255,255,255';
  /** Connecting-line colour (rgb triplet). */
  @Input() line = '255,210,0';

  private readonly canvasRef = viewChild.required<ElementRef<HTMLCanvasElement>>('cv');
  private readonly platformId = inject(PLATFORM_ID);
  private readonly zone = inject(NgZone);
  private readonly host = inject(ElementRef<HTMLElement>);

  private ctx: CanvasRenderingContext2D | null = null;
  private particles: Particle[] = [];
  private rafId = 0;
  private w = 0;
  private h = 0;
  private dpr = 1;
  private running = false;
  private visible = true;
  private resizeObs?: ResizeObserver;
  private io?: IntersectionObserver;
  private readonly onVisibility = () => this.evaluateRunning();

  ngAfterViewInit(): void {
    if (!isPlatformBrowser(this.platformId)) return;
    const canvas = this.canvasRef().nativeElement;
    this.ctx = canvas.getContext('2d');
    if (!this.ctx) return;

    this.measure();
    this.seed();

    const reduce = window.matchMedia?.('(prefers-reduced-motion: reduce)').matches;
    if (reduce) {
      this.drawFrame(false); // single static frame, no loop
      return;
    }

    this.zone.runOutsideAngular(() => {
      this.resizeObs = new ResizeObserver(() => { this.measure(); this.seed(); });
      this.resizeObs.observe(this.host.nativeElement);

      this.io = new IntersectionObserver(
        (e) => { this.visible = e[0]?.isIntersecting ?? false; this.evaluateRunning(); },
        { threshold: 0 },
      );
      this.io.observe(this.host.nativeElement);

      document.addEventListener('visibilitychange', this.onVisibility);
      this.evaluateRunning();
    });
  }

  private evaluateRunning(): void {
    const shouldRun = this.visible && document.visibilityState === 'visible';
    if (shouldRun && !this.running) {
      this.running = true;
      this.rafId = requestAnimationFrame(() => this.loop());
    } else if (!shouldRun && this.running) {
      this.running = false;
      cancelAnimationFrame(this.rafId);
    }
  }

  private measure(): void {
    const rect = this.host.nativeElement.getBoundingClientRect();
    this.dpr = Math.min(window.devicePixelRatio || 1, 2);
    this.w = Math.max(1, Math.floor(rect.width));
    this.h = Math.max(1, Math.floor(rect.height));
    const canvas = this.canvasRef().nativeElement;
    canvas.width = Math.floor(this.w * this.dpr);
    canvas.height = Math.floor(this.h * this.dpr);
    this.ctx?.setTransform(this.dpr, 0, 0, this.dpr, 0, 0);
  }

  private seed(): void {
    // Particle count scales with area but is hard-capped for perf.
    const target = Math.min(Math.round((this.w * this.h) / 14000), 72);
    this.particles = Array.from({ length: target }, () => ({
      x: Math.random() * this.w,
      y: Math.random() * this.h,
      vx: (Math.random() - 0.5) * 0.25,
      vy: (Math.random() - 0.5) * 0.25,
      r: Math.random() * 1.6 + 0.7,
      a: Math.random() * 0.5 + 0.25,
    }));
  }

  private loop(): void {
    if (!this.running) return;
    this.step();
    this.drawFrame(true);
    this.rafId = requestAnimationFrame(() => this.loop());
  }

  private step(): void {
    for (const p of this.particles) {
      p.x += p.vx; p.y += p.vy;
      if (p.x < 0 || p.x > this.w) p.vx *= -1;
      if (p.y < 0 || p.y > this.h) p.vy *= -1;
    }
  }

  private drawFrame(_animated: boolean): void {
    const ctx = this.ctx;
    if (!ctx) return;
    ctx.clearRect(0, 0, this.w, this.h);

    // Connecting lines between nearby particles (the "constellation").
    const maxDist = 130;
    for (let i = 0; i < this.particles.length; i++) {
      const a = this.particles[i];
      for (let j = i + 1; j < this.particles.length; j++) {
        const b = this.particles[j];
        const dx = a.x - b.x, dy = a.y - b.y;
        const dist = Math.hypot(dx, dy);
        if (dist > maxDist) continue;
        const alpha = (1 - dist / maxDist) * 0.18;
        ctx.strokeStyle = `rgba(${this.line},${alpha.toFixed(3)})`;
        ctx.lineWidth = 1;
        ctx.beginPath();
        ctx.moveTo(a.x, a.y);
        ctx.lineTo(b.x, b.y);
        ctx.stroke();
      }
    }

    // Dots.
    for (const p of this.particles) {
      ctx.fillStyle = `rgba(${this.dot},${p.a.toFixed(3)})`;
      ctx.beginPath();
      ctx.arc(p.x, p.y, p.r, 0, Math.PI * 2);
      ctx.fill();
    }
  }

  ngOnDestroy(): void {
    this.running = false;
    if (this.rafId) cancelAnimationFrame(this.rafId);
    this.resizeObs?.disconnect();
    this.io?.disconnect();
    if (isPlatformBrowser(this.platformId)) {
      document.removeEventListener('visibilitychange', this.onVisibility);
    }
  }
}
