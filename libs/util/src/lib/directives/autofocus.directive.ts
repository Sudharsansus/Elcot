import { Directive, ElementRef, AfterViewInit, inject } from '@angular/core';
import { DOCUMENT } from '@angular/common';

@Directive({ selector: '[avgcAutofocus]', standalone: true })
export class AutofocusDirective implements AfterViewInit {
  private readonly el = inject(ElementRef<HTMLElement>);
  private readonly doc = inject(DOCUMENT);
  ngAfterViewInit(): void {
    const reduced = this.doc.defaultView?.matchMedia('(prefers-reduced-motion: reduce)').matches;
    if (!reduced) setTimeout(() => this.el.nativeElement.focus());
  }
}
