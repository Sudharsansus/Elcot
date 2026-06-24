import { Directive, TemplateRef, ViewContainerRef, inject, input } from '@angular/core';
import { Role } from '@avgc-xr/api-contracts';

@Directive({ selector: '[avgcHasRole]', standalone: true })
export class HasRoleDirective {
  private readonly tpl = inject(TemplateRef<unknown>);
  private readonly vc = inject(ViewContainerRef);
  avgcHasRole = input.required<string>();
  avgcHasRoleInverse = input<boolean>(false);
  private roles: Role[] = [];

  constructor() {
    try { const s = sessionStorage.getItem('avgc_user_roles'); if (s) this.roles = JSON.parse(s); } catch { this.roles = []; }
  }

  updateView(): void {
    const required = this.avgcHasRole().split(',').map(r => r.trim().toUpperCase() as Role);
    const has = required.some(r => this.roles.includes(r));
    this.vc.clear();
    if (this.avgcHasRoleInverse() ? !has : has) this.vc.createEmbeddedView(this.tpl);
  }
}
