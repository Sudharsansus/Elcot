// ============================================================
// APP ROOT — cinematic shell: glass navbar + content + footer + Mira.
// Boots the Lenis smooth-scroll engine on the client.
// ============================================================
import { Component, inject, afterNextRender } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './shared/navbar/navbar.component';
import { FooterComponent } from './shared/footer/footer.component';
import { SmoothScrollService } from './core/services/smooth-scroll.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent, FooterComponent],
  template: `
    <a class="skip-link" href="#main-content">Skip to content</a>
    <app-navbar />
    <main id="main-content" class="main-content" role="main">
      <router-outlet />
    </main>
    <app-footer />
  `,
  styles: [`
    :host { display: flex; flex-direction: column; min-height: 100vh; }
    .main-content { flex: 1 0 auto; display: block; }
  `],
})
export class AppComponent {
  private readonly smooth = inject(SmoothScrollService);
  constructor() {
    afterNextRender(() => this.smooth.init());
  }
}
