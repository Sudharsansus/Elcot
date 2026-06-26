import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { environment } from '../../../environments/environment';
import { I18nService } from './i18n.service';

/**
 * Strapi 5 list response. NOTE: Strapi 5 **flattens** entries — fields sit at the
 * top level of each item alongside `id` and `documentId`. There is NO Strapi-4
 * `attributes` wrapper, so consumers read `item.title`, not `item.attributes.title`.
 */
export interface StrapiListResponse<T> {
  data: Array<T & { id: number; documentId: string }>;
  meta: {
    pagination?: { page: number; pageSize: number; pageCount: number; total: number };
  };
}

/** A populated Strapi 5 media field (flattened). */
export interface StrapiMedia {
  url: string;
  alternativeText?: string | null;
}

export interface NewsItem {
  id: number;
  documentId: string;
  title: string;
  slug: string;
  excerpt?: string;
  body?: string;
  coverImage?: StrapiMedia | null;
  category: 'announcement' | 'scheme-update' | 'industry-news' | 'press-release';
  publishedDate?: string;
  featured?: boolean;
}

export interface EventItem {
  id: number;
  documentId: string;
  title: string;
  slug: string;
  description?: string;
  eventDate: string;
  endDate?: string;
  location?: string;
  registrationUrl?: string;
  capacity?: number;
}

/**
 * Consumes the Strapi 5 headless CMS REST API for dynamic public content
 * (News, Events). Locale-aware (en/ta via {@link I18nService}). Fails soft:
 * on any error the signals hold an empty list and {@link error} is set, so
 * callers can fall back to static content without throwing.
 */
@Injectable({ providedIn: 'root' })
export class StrapiContentService {
  private readonly http = inject(HttpClient);
  private readonly i18n = inject(I18nService);

  /** Base Strapi REST URL. Dev: localhost:1337/api. Prod: proxied via nginx. */
  private readonly baseUrl = environment.strapiUrl;

  readonly news = signal<NewsItem[]>([]);
  readonly events = signal<EventItem[]>([]);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

  /** Fetch published news, newest first, in the current locale. */
  async fetchNews(options?: { featured?: boolean; limit?: number }): Promise<NewsItem[]> {
    this.loading.set(true);
    this.error.set(null);
    try {
      const params: Record<string, string> = {
        locale: this.i18n.currentLanguage(),
        populate: 'coverImage',
        sort: 'publishedDate:desc',
        'pagination[pageSize]': String(options?.limit ?? 10),
      };
      if (options?.featured) {
        params['filters[featured][$eq]'] = 'true';
      }
      const query = new URLSearchParams(params).toString();
      const res = await firstValueFrom(
        this.http.get<StrapiListResponse<NewsItem>>(`${this.baseUrl}/news-items?${query}`, {
          headers: { 'X-Silent-Error': '1' },
        }),
      );
      const items = res?.data ?? [];
      this.news.set(items);
      return items;
    } catch {
      this.error.set('Unable to load news from the CMS.');
      return [];
    } finally {
      this.loading.set(false);
    }
  }

  /** Fetch upcoming events, soonest first, in the current locale. */
  async fetchEvents(options?: { limit?: number }): Promise<EventItem[]> {
    this.loading.set(true);
    this.error.set(null);
    try {
      const params: Record<string, string> = {
        locale: this.i18n.currentLanguage(),
        sort: 'eventDate:asc',
        'pagination[pageSize]': String(options?.limit ?? 10),
      };
      const query = new URLSearchParams(params).toString();
      const res = await firstValueFrom(
        this.http.get<StrapiListResponse<EventItem>>(`${this.baseUrl}/events?${query}`, {
          headers: { 'X-Silent-Error': '1' },
        }),
      );
      const items = res?.data ?? [];
      this.events.set(items);
      return items;
    } catch {
      this.error.set('Unable to load events from the CMS.');
      return [];
    } finally {
      this.loading.set(false);
    }
  }
}
