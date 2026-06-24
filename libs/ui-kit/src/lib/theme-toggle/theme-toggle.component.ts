import { Component, ViewEncapsulation, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'avgc-theme-toggle',
  standalone: true, imports: [CommonModule],
  templateUrl: './theme-toggle.component.html',
  styleUrls: ['./theme-toggle.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class ThemeToggleComponent {
  isDark = signal(false);

  constructor() {
    // Check system preference
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
    const stored = localStorage.getItem('avgc_theme');
    this.isDark.set(stored === 'dark' || (!stored && prefersDark));
    this.applyTheme();
  }

  toggle(): void {
    this.isDark.update(v => !v);
    localStorage.setItem('avgc_theme', this.isDark() ? 'dark' : 'light');
    this.applyTheme();
  }

  private applyTheme(): void {
    document.documentElement.setAttribute('data-theme', this.isDark() ? 'dark' : 'light');
  }
}
