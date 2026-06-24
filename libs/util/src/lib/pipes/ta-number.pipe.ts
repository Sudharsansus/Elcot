import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'avgcTaNuumber', standalone: true, pure: true })
export class TaNumberPipe implements PipeTransform {
  private static DIGITS = ['\u0BE6','\u0BE7','\u0BE8','\u0BE9','\u0BEA','\u0BEB','\u0BEC','\u0BED','\u0BEE','\u0BEF'];

  transform(value: number | string): string {
    return String(value).split('').map(ch => {
      const d = parseInt(ch, 10);
      return !isNaN(d) ? TaNumberPipe.DIGITS[d] : ch;
    }).join('');
  }
}
