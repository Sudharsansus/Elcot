import { Injectable, signal, inject, effect } from '@angular/core';
import { DOCUMENT } from '@angular/common';

export type ThemeMode = 'light' | 'dark' | 'system';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private readonly document = inject(DOCUMENT) as Document;

  readonly theme = signal<ThemeMode>(this.getStoredTheme());
  readonly currentAppliedTheme = signal<'light' | 'dark'>(this.resolveTheme(this.theme()));

  constructor() {
    effect(() => {
      const resolved = this.resolveTheme(this.theme());
      this.currentAppliedTheme.set(resolved);
      this.applyTheme(resolved);
      localStorage.setItem('avgcxr-theme', this.theme());
    });

    window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', () => {
      if (this.theme() === 'system') {
        const resolved = this.resolveTheme('system');
        this.currentAppliedTheme.set(resolved);
        this.applyTheme(resolved);
      }
    });
  }

  toggleTheme(): void {
    const current = this.currentAppliedTheme();
    this.theme.set(current === 'light' ? 'dark' : 'light');
  }

  setTheme(mode: ThemeMode): void {
    this.theme.set(mode);
  }

  private applyTheme(resolved: 'light' | 'dark'): void {
    this.document.body.setAttribute('data-theme', resolved);
    const metaTheme = this.document.querySelector('meta[name="theme-color"]');
    if (metaTheme) {
      metaTheme.setAttribute('content', resolved === 'dark' ? '#120f1c' : '#7c3aed');
    }
  }

  private resolveTheme(mode: ThemeMode): 'light' | 'dark' {
    if (mode === 'system') {
      return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
    }
    return mode;
  }

  private getStoredTheme(): ThemeMode {
    const stored = localStorage.getItem('avgcxr-theme');
    if (stored === 'light' || stored === 'dark' || stored === 'system') return stored;
    return 'light';
  }
}
