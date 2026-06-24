import { Component, input, model, output, forwardRef, ViewEncapsulation } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

export interface SelectOption { value: string; label: string; labelTamil?: string; }

@Component({
  selector: 'avgc-select',
  standalone: true, imports: [CommonModule],
  templateUrl: './select.component.html',
  styleUrls: ['./select.component.scss'],
  encapsulation: ViewEncapsulation.None,
  providers: [{ provide: NG_VALUE_ACCESSOR, useExisting: forwardRef(() => SelectComponent), multi: true }],
})
export class SelectComponent implements ControlValueAccessor {
  label = input<string>('');
  labelTamil = input<string>('');
  placeholder = input<string>('Select an option');
  options = input<SelectOption[]>([]);
  required = input<boolean>(false);
  disabled = model<boolean>(false);
  error = input<string>('');
  valueChanged = output<string>();

  selectedValue = '';
  onTouched: () => void = () => {};
  onChange: (val: string) => void = () => {};

  writeValue(val: string): void { this.selectedValue = val ?? ''; }
  registerOnChange(fn: (val: string) => void): void { this.onChange = fn; }
  registerOnTouched(fn: () => void): void { this.onTouched = fn; }
  setDisabledState(isDisabled: boolean): void { this.disabled.set(isDisabled); }

  onSelect(event: Event): void {
    const val = (event.target as HTMLSelectElement).value;
    this.selectedValue = val;
    this.onChange(val);
    this.onTouched();
    this.valueChanged.emit(val);
  }
}
