import { Component, input, model, output, forwardRef, ViewEncapsulation } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
  selector: 'avgc-input',
  standalone: true, imports: [CommonModule],
  templateUrl: './input.component.html',
  styleUrls: ['./input.component.scss'],
  encapsulation: ViewEncapsulation.None,
  providers: [{ provide: NG_VALUE_ACCESSOR, useExisting: forwardRef(() => InputComponent), multi: true }],
})
export class InputComponent implements ControlValueAccessor {
  label = input<string>('');
  labelTamil = input<string>('');
  placeholder = input<string>('');
  type = input<'text' | 'email' | 'password' | 'number' | 'tel' | 'url'>('text');
  required = input<boolean>(false);
  disabled = model<boolean>(false);
  readonly = input<boolean>(false);
  error = input<string>('');
  hint = input<string>('');
  maxLength = input<number>(0);
  icon = input<string>('');
  valueChanged = output<string>();

  value = '';
  onTouched: () => void = () => {};
  onChange: (val: string) => void = () => {};

  writeValue(val: string): void { this.value = val ?? ''; }
  registerOnChange(fn: (val: string) => void): void { this.onChange = fn; }
  registerOnTouched(fn: () => void): void { this.onTouched = fn; }
  setDisabledState(isDisabled: boolean): void { this.disabled.set(isDisabled); }

  onInput(event: Event): void {
    const val = (event.target as HTMLInputElement).value;
    this.value = val;
    this.onChange(val);
    this.onTouched();
    this.valueChanged.emit(val);
  }

  onBlur(): void { this.onTouched(); }
}
