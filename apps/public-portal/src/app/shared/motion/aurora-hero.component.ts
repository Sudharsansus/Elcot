// ============================================================
// AURORA HERO — cinematic particle constellation behind the hero.
// Pure Canvas2D (no three.js): cursor-reactive depth field with additive
// glow + constellation links. Browser-only, zoneless RAF, reduced-motion safe.
// ============================================================
import {
  Component, ChangeDetectionStrategy, ElementRef, viewChild,
  afterNextRender, inject, NgZone, OnDestroy, input,
} from '@angular/core';

interface P { x: number; y: number; vx: number; vy: number; z: number; r: number; c: string; }

@Component({
  selector: 'app-aurora-hero',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<canvas #cv aria-hidden="true"></canvas>`,
  styles: [`
    :host { position: absolute; inset: 0; display: block; overflow: hidden; pointer-events: none; }
    canvas { width: 100%; height: 100%; display: block; }
  `],
})
export class AuroraHeroComponent implements OnDestroy {
  /** particle density multiplier (1 = default) */
  readonly density = input(1);
  private readonly cv = viewChild.required<ElementRef<HTMLCanvasElement>>('cv');
  private readonly zone = inject(NgZone);

  private raf = 0;
  private ro?: ResizeObserver;
  private dispose: Array<() => void> = [];
  private particles: P[] = [];
  private w = 0; private h = 0; private dpr = 1;
  private mx = -9999; private my = -9999; private tmx = -9999; private tmy = -9999;
  private readonly palette = ['#c8102e', '#ff6a3d', '#ffc14d', '#0e9e8e', '#1e66f5'];

  constructor() {
    afterNextRender(() => this.zone.runOutsideAngular(() => this.boot()));
  }

  private boot(): void {
    const canvas = this.cv().nativeElement;
    const ctx = canvas.getContext('2d', { alpha: true });
    if (!ctx) return;

    const reduce = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    this.dpr = Math.min(window.devicePixelRatio || 1, 2);

    const resize = () => {
      const host = canvas.parentElement ?? canvas;
      this.w = host.clientWidth; this.h = host.clientHeight;
      canvas.width = Math.floor(this.w * this.dpr);
      canvas.height = Math.floor(this.h * this.dpr);
      ctx.setTransform(this.dpr, 0, 0, this.dpr, 0, 0);
      this.seed();
    };
    resize();
    this.ro = new ResizeObserver(resize);
    this.ro.observe(canvas.parentElement ?? canvas);

    const onMove = (e: PointerEvent) => {
      const rect = canvas.getBoundingClientRect();
      this.tmx = e.clientX - rect.left; this.tmy = e.clientY - rect.top;
    };
    const onLeave = () => { this.tmx = -9999; this.tmy = -9999; };
    window.addEventListener('pointermove', onMove, { passive: true });
    window.addEventListener('pointerout', onLeave, { passive: true });
    this.dispose.push(
      () => window.removeEventListener('pointermove', onMove),
      () => window.removeEventListener('pointerout', onLeave),
    );

    if (reduce) { this.draw(ctx); return; }
    const loop = () => { this.step(); this.draw(ctx); this.raf = requestAnimationFrame(loop); };
    this.raf = requestAnimationFrame(loop);
  }

  private seed(): void {
    const target = Math.round((this.w * this.h) / 14000 * this.density());
    const n = Math.max(28, Math.min(150, target));
    this.particles = Array.from({ length: n }, () => {
      const z = Math.random() * 0.9 + 0.1;
      return {
        x: Math.random() * this.w, y: Math.random() * this.h,
        vx: (Math.random() - 0.5) * 0.16, vy: (Math.random() - 0.5) * 0.16,
        z, r: z * 1.9 + 0.4,
        c: this.palette[(Math.random() * this.palette.length) | 0],
      };
    });
  }

  private step(): void {
    this.mx += (this.tmx - this.mx) * 0.08;
    this.my += (this.tmy - this.my) * 0.08;
    for (const p of this.particles) {
      p.x += p.vx * p.z; p.y += p.vy * p.z;
      // gentle cursor attraction (parallax by depth)
      const dx = this.mx - p.x, dy = this.my - p.y;
      const d2 = dx * dx + dy * dy;
      if (d2 < 26000) { const f = (1 - d2 / 26000) * 0.6 * p.z; p.x += dx * 0.0009 * f * 60; p.y += dy * 0.0009 * f * 60; }
      // wrap
      if (p.x < -20) p.x = this.w + 20; if (p.x > this.w + 20) p.x = -20;
      if (p.y < -20) p.y = this.h + 20; if (p.y > this.h + 20) p.y = -20;
    }
  }

  private draw(ctx: CanvasRenderingContext2D): void {
    ctx.clearRect(0, 0, this.w, this.h);
    const ps = this.particles;

    // constellation links (soft warm, source-over for a light ground)
    for (let i = 0; i < ps.length; i++) {
      for (let j = i + 1; j < ps.length; j++) {
        const a = ps[i], b = ps[j];
        const dx = a.x - b.x, dy = a.y - b.y; const d2 = dx * dx + dy * dy;
        if (d2 < 15000) {
          const alpha = (1 - d2 / 15000) * 0.10;
          ctx.strokeStyle = `rgba(150, 60, 50, ${alpha})`;
          ctx.lineWidth = 0.6;
          ctx.beginPath(); ctx.moveTo(a.x, a.y); ctx.lineTo(b.x, b.y); ctx.stroke();
        }
      }
    }
    // soft glowing warm nodes
    for (const p of ps) {
      const g = ctx.createRadialGradient(p.x, p.y, 0, p.x, p.y, p.r * 7);
      g.addColorStop(0, this.hexA(p.c, 0.55));
      g.addColorStop(0.5, this.hexA(p.c, 0.16));
      g.addColorStop(1, 'rgba(0,0,0,0)');
      ctx.fillStyle = g;
      ctx.beginPath(); ctx.arc(p.x, p.y, p.r * 7, 0, Math.PI * 2); ctx.fill();
      ctx.fillStyle = this.hexA(p.c, 0.9);
      ctx.beginPath(); ctx.arc(p.x, p.y, p.r * 0.8, 0, Math.PI * 2); ctx.fill();
    }
  }

  private hexA(hex: string, a: number): string {
    const n = parseInt(hex.slice(1), 16);
    return `rgba(${(n >> 16) & 255},${(n >> 8) & 255},${n & 255},${a})`;
  }

  ngOnDestroy(): void {
    cancelAnimationFrame(this.raf);
    this.ro?.disconnect();
    this.dispose.forEach((d) => d());
  }
}
