// ============================================================
// APP ROOT — cinematic shell: glass navbar + content + footer + Mira.
// Boots the Lenis smooth-scroll engine on the client.
// Auth routes (/auth/*) render chrome-free: the focused full-screen
// AuthShell carries its own brand, so the marketing nav/footer/Mira hide.
// ============================================================
import { Component, inject, afterNextRender } from '@angular/core';
import { RouterOutlet, Router, NavigationEnd } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { filter, map } from 'rxjs';
import { TopBarComponent } from './shared/top-bar/top-bar.component';
import { NavbarComponent } from './shared/navbar/navbar.component';
import { FooterComponent } from './shared/footer/footer.component';
import { MiraComponent } from './features/chat/mira/mira.component';
import { SmoothScrollService } from './core/services/smooth-scroll.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, TopBarComponent, NavbarComponent, FooterComponent, MiraComponent],
  template: `
    <a class="skip-link" href="#main-content">Skip to content</a>
    @if (!isAuthRoute()) {
      <app-top-bar />
      <app-navbar />
    }
    <main id="main-content" class="main-content" role="main">
      <router-outlet />
    </main>
    @if (!isAuthRoute()) {
      <app-footer />
    }
    <app-mira />
  `,
  styles: [`
    :host { display: flex; flex-direction: column; min-height: 100vh; }
    .main-content { flex: 1 0 auto; display: block; }
  `],
})
export class AppComponent {
  private readonly smooth = inject(SmoothScrollService);
  private readonly router = inject(Router);

  /** True while on an /auth/* route — used to hide the marketing chrome. */
  readonly isAuthRoute = toSignal(
    this.router.events.pipe(
      filter((e): e is NavigationEnd => e instanceof NavigationEnd),
      map(() => this.router.url.startsWith('/auth')),
    ),
    { initialValue: this.router.url.startsWith('/auth') },
  );

  constructor() {
    afterNextRender(() => this.smooth.init());
  }
}
