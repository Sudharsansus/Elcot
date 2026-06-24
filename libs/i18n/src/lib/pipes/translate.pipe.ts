import { Pipe, PipeTransform, inject } from '@angular/core';
import { TranslationService } from '../services/translation.service';

@Pipe({ name: 'avgcTranslate', standalone: true, pure: false })
export class TranslatePipe implements PipeTransform {
  private readonly svc = inject(TranslationService);
  transform(key: string, params?: Record<string, string | number>): string { return this.svc.translate(key, params); }
}
