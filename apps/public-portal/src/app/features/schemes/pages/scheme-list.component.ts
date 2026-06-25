// ============================================================
// PUBLIC PORTAL — SCHEMES LIST PAGE
// ============================================================
// Drop into: apps/public-portal/src/app/features/schemes/pages/
// Replaces the existing scheme-list component.
// ============================================================

import { Component, ChangeDetectionStrategy, inject, signal, computed, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatChipsModule } from '@angular/material/chips';
import { I18nService } from '../../../core/services/i18n.service';
import { SCHEMES_DATA, Scheme } from '../schemes.data';
import { StateMessageComponent } from '../../../shared/state-message.component';
import { PageHeaderComponent, Crumb } from '../../../shared/page-header/page-header.component';

@Component({
  selector: 'app-scheme-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule, RouterModule, ReactiveFormsModule,
    MatIconModule, MatButtonModule, MatSelectModule,
    MatFormFieldModule, MatInputModule, MatChipsModule,
    StateMessageComponent, PageHeaderComponent
  ],
  templateUrl: './scheme-list.component.html',
  styleUrls: ['./scheme-list.component.scss']
})
export class SchemeListComponent implements OnInit {
  private readonly i18n = inject(I18nService);
  private readonly fb = inject(FormBuilder);

  readonly lang = this.i18n.currentLanguage;
  readonly allSchemes = signal<Scheme[]>(SCHEMES_DATA);
  readonly searchTerm = signal('');
  readonly selectedCategory = signal<string>('');
  readonly selectedStatus = signal<string>('');
  readonly selectedDepartment = signal<string>('');

  readonly filterForm: FormGroup = this.fb.group({
    search: [''],
    category: [''],
    status: [''],
    department: ['']
  });

  get searchControl(): FormControl { return this.filterForm.get('search') as FormControl; }
  get categoryControl(): FormControl { return this.filterForm.get('category') as FormControl; }
  get statusControl(): FormControl { return this.filterForm.get('status') as FormControl; }
  get departmentControl(): FormControl { return this.filterForm.get('department') as FormControl; }

  readonly categories = [
    { value: 'production', icon: 'movie_filter', color: 'var(--color-sector-animation)' },
    { value: 'training', icon: 'school', color: 'var(--color-sector-gaming)' },
    { value: 'infrastructure', icon: 'view_in_ar', color: 'var(--color-sector-xr)' },
    { value: 'export', icon: 'public', color: 'var(--color-sector-vfx)' },
    { value: 'freelancer', icon: 'person', color: 'var(--color-sector-comics)' },
    { value: 'scholarship', icon: 'menu_book', color: 'var(--color-primary)' }
  ];

  readonly statuses = [
    { value: 'active', labelEn: 'Active', labelTa: 'செயலில்' },
    { value: 'upcoming', labelEn: 'Upcoming', labelTa: 'வரவிருக்கும்' },
    { value: 'closed', labelEn: 'Closed', labelTa: 'மூடப்பட்டது' }
  ];

  readonly departments = Array.from(
    new Set(SCHEMES_DATA.map((s: Scheme) => s.department))
  );

  readonly filteredSchemes = computed(() => {
    const search = this.searchTerm().toLowerCase().trim();
    const category = this.selectedCategory();
    const status = this.selectedStatus();
    const department = this.selectedDepartment();

    return this.allSchemes().filter(s => {
      if (category && s.category !== category) return false;
      if (status && s.status !== status) return false;
      if (department && s.department !== department) return false;
      if (search) {
        const name = this.lang() === 'ta' ? s.nameTa : s.name;
        const desc = this.lang() === 'ta' ? s.descriptionTa : s.description;
        return (
          name.toLowerCase().includes(search) ||
          desc.toLowerCase().includes(search) ||
          s.department.toLowerCase().includes(search)
        );
      }
      return true;
    });
  });

  readonly hasActiveFilters = computed(() =>
    !!(this.searchTerm() || this.selectedCategory() || this.selectedStatus() || this.selectedDepartment())
  );

  readonly isTa = computed(() => this.lang() === 'ta');
  readonly crumbs = computed<Crumb[]>(() => [{ label: this.t('schemes.title') }]);

  ngOnInit(): void {
    this.searchControl.valueChanges.subscribe(v => this.searchTerm.set(v || ''));
    this.categoryControl.valueChanges.subscribe(v => this.selectedCategory.set(v || ''));
    this.statusControl.valueChanges.subscribe(v => this.selectedStatus.set(v || ''));
    this.departmentControl.valueChanges.subscribe(v => this.selectedDepartment.set(v || ''));
  }

  resetFilters(): void {
    this.filterForm.reset();
    this.searchTerm.set('');
    this.selectedCategory.set('');
    this.selectedStatus.set('');
    this.selectedDepartment.set('');
  }

  getName(s: Scheme): string {
    return this.lang() === 'ta' ? s.nameTa : s.name;
  }

  getDescription(s: Scheme): string {
    return this.lang() === 'ta' ? s.descriptionTa : s.description;
  }

  getDepartment(s: Scheme): string {
    return this.lang() === 'ta' ? s.departmentTa : s.department;
  }

  getCategoryLabel(s: Scheme): string {
    return this.t(`schemes.categories.${s.category}`);
  }

  getStatusLabel(status: string): string {
    const s = this.statuses.find(x => x.value === status);
    if (!s) return status;
    return this.lang() === 'ta' ? s.labelTa : s.labelEn;
  }

  formatAmount(amount: number): string {
    if (amount >= 10000000) {
      return `₹${(amount / 10000000).toFixed(amount % 10000000 === 0 ? 0 : 1)} Crore`;
    }
    if (amount >= 100000) {
      return `₹${(amount / 100000).toFixed(amount % 100000 === 0 ? 0 : 1)} Lakh`;
    }
    return `₹${amount.toLocaleString('en-IN')}`;
  }

  formatDate(dateStr: string): string {
    const d = new Date(dateStr);
    return d.toLocaleDateString(this.lang() === 'ta' ? 'ta-IN' : 'en-IN', {
      year: 'numeric', month: 'long', day: 'numeric'
    });
  }

  t(key: string): string { return this.i18n.translate(key); }
}
