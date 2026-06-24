import { Directive, inject, input, TemplateRef, ViewContainerRef } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';

@Directive({
  selector: '[hasRole]',
  standalone: true
})
export class HasRoleDirective {
  private readonly templateRef = inject(TemplateRef);
  private readonly viewContainer = inject(ViewContainerRef);
  private readonly authService = inject(AuthService);

  readonly hasRole = input.required<string | string[]>();

  constructor() {
    this.updateView();
  }

  private updateView(): void {
    const requiredRoles = typeof this.hasRole() === 'string'
      ? [this.hasRole()]
      : this.hasRole();

    const user = this.authService.currentUser();
    const hasAccess = user?.roles?.some(role => requiredRoles.includes(role)) ?? false;

    this.viewContainer.clear();
    if (hasAccess) {
      this.viewContainer.createEmbeddedView(this.templateRef);
    }
  }
}
