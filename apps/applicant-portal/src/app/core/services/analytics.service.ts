import { Injectable, inject } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { filter } from 'rxjs/operators';

interface AnalyticsEvent {
  category: string;
  action: string;
  label?: string;
  value?: number;
  timestamp: string;
  url: string;
  userId?: string;
}

@Injectable({ providedIn: 'root' })
export class AnalyticsService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly events: AnalyticsEvent[] = [];
  private readonly MAX_BATCH_SIZE = 20;
  private flushTimerId: ReturnType<typeof setInterval> | null = null;

  constructor() {
    if (environment.production) {
      this.router.events.pipe(
        filter((event): event is NavigationEnd => event instanceof NavigationEnd)
      ).subscribe((event) => {
        this.trackPageView(event.urlAfterRedirects);
      });
    }
  }

  trackPageView(url: string): void {
    this.pushEvent({
      category: 'pageview',
      action: 'navigate',
      label: url,
      timestamp: new Date().toISOString(),
      url
    });
  }

  trackEvent(category: string, action: string, label?: string, value?: number): void {
    this.pushEvent({
      category,
      action,
      label,
      value,
      timestamp: new Date().toISOString(),
      url: this.router.url
    });
  }

  trackFormSubmission(formName: string, success: boolean = true): void {
    this.pushEvent({
      category: 'form',
      action: success ? 'submit_success' : 'submit_error',
      label: formName,
      timestamp: new Date().toISOString(),
      url: this.router.url
    });
  }

  private pushEvent(event: AnalyticsEvent): void {
    this.events.push(event);

    if (this.events.length >= this.MAX_BATCH_SIZE) {
      this.flush();
      return;
    }

    if (!this.flushTimerId) {
      this.flushTimerId = setInterval(() => this.flush(), 30000);
    }
  }

  private flush(): void {
    if (this.flushTimerId) {
      clearInterval(this.flushTimerId);
      this.flushTimerId = null;
    }

    if (this.events.length === 0) return;

    const batch = [...this.events];
    this.events.length = 0;

    this.http.post(`${environment.apiUrl}/analytics/events`, { events: batch }).subscribe({
      error: () => { /* Silent fail for analytics */ }
    });
  }
}
