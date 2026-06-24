import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiClientService } from '@avgc-xr/data-access';
import { ButtonComponent, TableColumn, TableComponent } from '@avgc-xr/ui-kit';

@Component({ selector: 'app-user-management', standalone: true, imports: [ButtonComponent, CommonModule, TableComponent], templateUrl: './user-management.component.html' })
export class UserManagementComponent {
  private api = inject(ApiClientService);
  columns: TableColumn[] = [{ key: 'fullName', label: 'Name', sortable: true }, { key: 'email', label: 'Email' }, { key: 'roles', label: 'Roles' }, { key: 'status', label: 'Status' }, { key: 'district', label: 'District' }];
  users = signal<any[]>([]); loading = signal(false);
  async ngOnInit() { this.loading.set(true); try { const r = await this.api.getPage('/users', 0, 20).toPromise(); this.users.set(r?.content ?? []); } finally { this.loading.set(false); } }
}
