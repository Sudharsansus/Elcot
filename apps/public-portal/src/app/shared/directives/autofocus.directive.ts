import { Directive, ElementRef, inject, AfterViewInit } from '@angular/core';

@Directive({
  selector: '[appAutofocus]',
  standalone: true
})
export class AutofocusDirective implements AfterViewInit {
  private readonly el = inject(ElementRef);

  ngAfterViewInit(): void {
    // Delay to ensure DOM is ready
    setTimeout(() => {
      this.el.nativeElement.focus();
    }, 100);
  }
}
