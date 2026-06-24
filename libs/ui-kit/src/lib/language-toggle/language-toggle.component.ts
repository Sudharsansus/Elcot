import { Component, output, inject, ViewEncapsulation } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslationService } from '@avgc-xr/i18n';

@Component({
  selector: 'avgc-language-toggle',
  standalone: true, imports: [CommonModule],
  templateUrl: './language-toggle.component.html',
  styleUrls: ['./language-toggle.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class LanguageToggleComponent {
  private translationSvc = inject(TranslationService);
  localeChanged = output<'en' | 'ta'>();
  currentLocale = this.translationSvc.currentLocale;

  toggle(): void {
    const newLocale = this.translationSvc.currentLocale() === 'en' ? 'ta' : 'en';
    this.translationSvc.setLocale(newLocale);
    this.localeChanged.emit(newLocale);
  }
}
