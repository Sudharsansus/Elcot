import { Component, inject, OnInit, signal, computed } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { ApiService, PaginatedResponse, PaginationParams } from '../../../core/services/api.service';
import { AnalyticsService } from '../../../core/services/analytics.service';
import { Scheme } from '@avgcxr/api-contracts';

@Component({
  selector: 'app-scheme-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule, DatePipe, MatIconModule, MatButtonModule, MatSelectModule, MatFormFieldModule, MatInputModule, MatProgressSpinnerModule, MatChipsModule],
  templateUrl: './scheme-list.component.html',
  styleUrls: ['./scheme-list.component.scss']
})
export class SchemeListComponent implements OnInit {
  private readonly api = inject(ApiService);
  private readonly analytics = inject(AnalyticsService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);

  readonly schemes = signal<Scheme[]>([]);
  readonly loading = signal(true);
  readonly total = signal(0);
  readonly page = signal(1);
  readonly pageSize = signal(12);
  readonly searchQuery = signal('');
  readonly isTamil = signal(false);

  readonly filterForm: FormGroup = this.fb.group({
    category: [''],
    status: [''],
    district: [''],
    search: ['']
  });

  get searchControl(): FormControl { return this.filterForm.get('search') as FormControl; }
  get categoryControl(): FormControl { return this.filterForm.get('category') as FormControl; }
  get statusControl(): FormControl { return this.filterForm.get('status') as FormControl; }

  readonly categories: { value: string; label: string; labelTa: string }[] = [
    { value: 'ANIMATION', label: 'Animation', labelTa: 'அனிமேஷன்' },
    { value: 'VFX', label: 'VFX', labelTa: 'காட்சி விளைவுகள்' },
    { value: 'GAMING', label: 'Gaming', labelTa: 'கேமிங்' },
    { value: 'COMICS', label: 'Comics', labelTa: 'காமிக்ஸ்' },
    { value: 'XR', label: 'Extended Reality', labelTa: 'நீட்டிக்கப்பட்ட யதார்த்தம்' },
  ];

  readonly statusOptions: { value: string; label: string; labelTa: string }[] = [
    { value: 'OPEN', label: 'Open', labelTa: 'திறந்துள்ளது' },
    { value: 'CLOSED', label: 'Closed', labelTa: 'மூடப்பட்டது' },
    { value: 'UPCOMING', label: 'Upcoming', labelTa: 'வரவிருக்கும்' },
  ];

  readonly totalPages = computed(() => Math.ceil(this.total() / this.pageSize()));

  ngOnInit(): void {
    this.analytics.trackPageView('/schemes');
    const lang = localStorage.getItem('avgcxr-lang');
    this.isTamil.set(lang === 'ta');

    this.route.queryParams.subscribe(params => {
      if (params['category']) this.filterForm.patchValue({ category: params['category'] });
      this.loadSchemes();
    });

    this.filterForm.valueChanges.subscribe(() => {
      this.page.set(1);
      this.loadSchemes();
    });
  }

  loadSchemes(): void {
    this.loading.set(true);
    const { category, status, district, search } = this.filterForm.value;
    const filters: Record<string, string> = {};
    if (category) filters['category'] = category;
    if (status) filters['status'] = status;
    if (district) filters['district'] = district;

    const pagination: PaginationParams = {
      page: this.page(),
      pageSize: this.pageSize(),
      sort: 'createdAt',
      sortDir: 'desc'
    };

    const searchParams = search ? { ...filters, search } : filters;

    this.api.getPaginated<Scheme>('/schemes', pagination, searchParams).subscribe({
      next: (response: PaginatedResponse<Scheme>) => {
        this.schemes.set(response.data);
        this.total.set(response.total);
        this.loading.set(false);
      },
      error: () => {
        this.schemes.set([]);
        this.loading.set(false);
      }
    });
  }

  onSearchInput(value: string): void {
    this.searchQuery.set(value);
    if (value.length === 0 || value.length >= 3) {
      this.page.set(1);
      this.loadSchemes();
    }
  }

  goToPage(p: number): void {
    this.page.set(p);
    this.loadSchemes();
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  clearFilters(): void {
    this.filterForm.reset();
    this.router.navigate([], { relativeTo: this.route });
  }

  getSchemeTitle(scheme: Scheme): string {
    return (this.isTamil() && scheme.nameTamil) ? scheme.nameTamil : scheme.name;
  }
}
