import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { ApiService } from '../../../core/services/api.service';
import { AnalyticsService } from '../../../core/services/analytics.service';
import { Scheme, SchemeCategory } from '@avgcxr/api-contracts';

interface HomeStats {
  totalSchemes: number;
  totalBeneficiaries: number;
  totalFunding: number;
  totalCompanies: number;
}

const SECTOR_CATEGORIES = [
  { key: 'ANIMATION' as SchemeCategory, icon: 'movie', label: 'Animation', labelTa: 'அனிமேஷன்', color: '#e91e63' },
  { key: 'VFX' as SchemeCategory, icon: 'auto_fix_high', label: 'VFX', labelTa: 'காட்சி விளைவுகள்', color: '#9c27b0' },
  { key: 'GAMING' as SchemeCategory, icon: 'sports_esports', label: 'Gaming', labelTa: 'கேமிங்', color: '#2196f3' },
  { key: 'COMICS' as SchemeCategory, icon: 'auto_stories', label: 'Comics', labelTa: 'காமிக்ஸ்', color: '#ff9800' },
  { key: 'XR' as SchemeCategory, icon: 'view_in_ar', label: 'Extended Reality', labelTa: 'நீட்டிக்கப்பட்ட யதார்த்தம்', color: '#4caf50' },
];

const CATEGORY_COLORS: Record<string, string> = {
  ANIMATION: '#e91e63', VFX: '#9c27b0', GAMING: '#2196f3', COMICS: '#ff9800', XR: '#4caf50'
};

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, MatIconModule, MatButtonModule, DatePipe],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  private readonly api = inject(ApiService);
  private readonly analytics = inject(AnalyticsService);

  readonly featuredSchemes = signal<Scheme[]>([]);
  readonly stats = signal<HomeStats>({ totalSchemes: 12, totalBeneficiaries: 350, totalFunding: 150, totalCompanies: 85 });
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);
  readonly isTamil = signal(false);
  readonly sectors = SECTOR_CATEGORIES;

  ngOnInit(): void {
    this.analytics.trackPageView('/');
    const lang = localStorage.getItem('avgcxr-lang');
    this.isTamil.set(lang === 'ta');
    this.loadStats();
    this.loadFeaturedSchemes();
  }

  private loadStats(): void {
    this.api.get<HomeStats>('/public/stats').subscribe({
      next: (data) => this.stats.set(data),
      error: () => { /* Using fallback stats */ }
    });
  }

  private loadFeaturedSchemes(): void {
    this.api.get<Scheme[]>('/schemes', { featured: 'true', pageSize: 6 }).subscribe({
      next: (data) => {
        const schemes = Array.isArray(data) ? data : (data as unknown as { data: Scheme[] }).data ?? [];
        this.featuredSchemes.set(schemes);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Failed to load schemes');
        this.loading.set(false);
      }
    });
  }

  getSchemeTitle(scheme: Scheme): string {
    return (this.isTamil() && scheme.nameTamil) ? scheme.nameTamil : scheme.name;
  }

  getCategoryColor(category: string): string {
    return CATEGORY_COLORS[category] || 'var(--color-primary)';
  }
}
