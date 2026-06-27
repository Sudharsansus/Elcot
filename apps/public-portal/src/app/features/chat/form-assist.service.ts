// ============================================================
// FORM ASSIST — lets a form hand itself to Mira so she can guide the
// user field-by-field, validate each answer, explain mistakes, and fill
// the fields live. A form registers a spec on enter and clears it on leave.
// ============================================================
import { Injectable, signal } from '@angular/core';

export interface AssistField {
  key: string;
  labelEn: string;
  labelTa: string;
  type?: 'text' | 'email' | 'tel' | 'password' | 'select';
  options?: string[]; // for select fields (friendly labels)
  /** Return a short reason string if invalid, or null if OK. */
  validate?: (value: string) => string | null;
}

export interface AssistSpec {
  titleEn: string;
  titleTa: string;
  fields: AssistField[];
  /** Apply a validated value to the underlying form control. */
  setValue: (key: string, value: string) => void;
  /** Read the current control value (for re-prompts / confirmation). */
  getValue: (key: string) => string;
  /** Submit the form (optional). */
  submit?: () => void;
}

@Injectable({ providedIn: 'root' })
export class FormAssistService {
  /** The form currently on screen that opted into Mira assistance (or null). */
  readonly active = signal<AssistSpec | null>(null);
  /** Bumped when the user taps "Fill with Mira" — Mira watches this. */
  readonly startTick = signal(0);

  register(spec: AssistSpec): void {
    this.active.set(spec);
  }
  clear(spec?: AssistSpec): void {
    // only clear if the leaving form is the one registered (avoids races)
    if (!spec || this.active() === spec) this.active.set(null);
  }
  /** Ask Mira to begin the guided fill of the active form. */
  begin(): void {
    if (this.active()) this.startTick.update((n) => n + 1);
  }
}
