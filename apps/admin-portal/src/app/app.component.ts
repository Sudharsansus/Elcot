import { Component, inject, signal, computed, OnInit, DestroyRef } from '@angular/core';
import { RouterOutlet, Router, NavigationEnd } from '@angular/router';
import { DOCUMENT } from '@angular/common';
import { RouterModule } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { takeUntilDestroyed, toSignal } from "@angular/core/rxjs-interop";
import { filter, map } from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  private readonly destroyRef = inject(DestroyRef);
  private readonly document = inject(DOCUMENT) as Document;
  private readonly translate = inject(TranslateService);
  private readonly router = inject(Router);

  /** Hide the dashboard chrome (header/footer) on /auth/* screens. */
  readonly isAuthRoute = toSignal(
    this.router.events.pipe(
      filter((e): e is NavigationEnd => e instanceof NavigationEnd),
      map(() => this.router.url.startsWith('/auth')),
    ),
    { initialValue: this.router.url.startsWith('/auth') },
  );

  readonly title = signal('Tamil Nadu AVGC-XR Portal');
  readonly currentLanguage = signal<string>('en');
  readonly isTamil = computed(() => this.currentLanguage() === 'ta');
  readonly currentYear = new Date().getFullYear();
  readonly mobileMenuOpen = signal(false);

  ngOnInit(): void {
    const savedLang = localStorage.getItem('avgcxr-lang') || 'en';
    this.currentLanguage.set(savedLang);
    this.translate.setDefaultLang('en');
    this.translate.use(savedLang);
    this.document.documentElement.lang = savedLang;

    this.translate.onLangChange.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((event) => {
      this.currentLanguage.set(event.lang);
      this.document.documentElement.lang = event.lang;
      localStorage.setItem('avgcxr-lang', event.lang);
    });
  }

  toggleLanguage(): void {
    const newLang = this.currentLanguage() === 'en' ? 'ta' : 'en';
    this.translate.use(newLang);
  }

  toggleMobileMenu(): void {
    this.mobileMenuOpen.update(v => !v);
  }
}
