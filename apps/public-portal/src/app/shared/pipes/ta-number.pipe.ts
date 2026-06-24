import { Pipe, PipeTransform } from '@angular/core';

const TAMIL_DIGITS: Record<string, string> = {
  '0': '௦', '1': '௧', '2': '௨', '3': '௩', '4': '௪',
  '5': '௫', '6': '௬', '7': '௭', '8': '௮', '9': '௯'
};

@Pipe({
  name: 'taNumber',
  standalone: true
})
export class TaNumberPipe implements PipeTransform {
  transform(value: number | string): string {
    if (value === null || value === undefined) return '';
    const str = String(value);
    return str.replace(/[0-9]/g, (digit) => TAMIL_DIGITS[digit] || digit);
  }
}
