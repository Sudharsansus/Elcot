import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { PageResponse } from '@avgc-xr/api-contracts';
import { API_BASE_URL } from '../tokens/api-base-url.token';

@Injectable({ providedIn: 'root' })
export class ApiClientService {
  private readonly http = inject(HttpClient);
  // Real backend base (provided per-app); falls back to same-origin /api/v1.
  private readonly API_BASE = inject(API_BASE_URL);

  get<T>(endpoint: string, params?: Record<string, string | number | boolean>): Observable<T> {
    let hp = new HttpParams();
    if (params) Object.entries(params).forEach(([k, v]) => { if (v != null) hp = hp.set(k, String(v)); });
    return this.http.get<T>(`${this.API_BASE}${endpoint}`, { params: hp });
  }

  getPage<T>(endpoint: string, page: number, size: number, extra?: Record<string, string>): Observable<PageResponse<T>> {
    // The API returns the rows in `data` (unwrapped to an array by the app's
    // response interceptor). Normalise both that and a real page object into a
    // PageResponse so list components can read .content / .totalElements.
    return this.get<unknown>(endpoint, { page, size, ...extra }).pipe(
      map((resp): PageResponse<T> => {
        const content: T[] = Array.isArray(resp)
          ? (resp as T[])
          : (((resp as Partial<PageResponse<T>>)?.content) ?? []);
        const r = (Array.isArray(resp) ? {} : (resp ?? {})) as Partial<PageResponse<T>>;
        const totalElements = r.totalElements ?? content.length;
        return {
          content,
          totalElements,
          totalPages: r.totalPages ?? Math.max(1, Math.ceil(totalElements / (size || 10))),
          currentPage: r.currentPage ?? page,
          pageSize: r.pageSize ?? size,
          hasNext: r.hasNext ?? false,
          hasPrevious: r.hasPrevious ?? page > 0,
        };
      }),
    );
  }

  getById<T>(endpoint: string, id: string): Observable<T> { return this.http.get<T>(`${this.API_BASE}${endpoint}/${id}`); }

  post<T>(endpoint: string, body: unknown): Observable<T> { return this.http.post<T>(`${this.API_BASE}${endpoint}`, body); }

  put<T>(endpoint: string, id: string, body: unknown): Observable<T> { return this.http.put<T>(`${this.API_BASE}${endpoint}/${id}`, body); }

  patch<T>(endpoint: string, id: string, body: unknown): Observable<T> { return this.http.patch<T>(`${this.API_BASE}${endpoint}/${id}`, body); }

  delete(endpoint: string, id: string): Observable<void> { return this.http.delete<void>(`${this.API_BASE}${endpoint}/${id}`); }

  upload<T>(endpoint: string, file: File, field = 'file', extra?: Record<string, string>): Observable<T> {
    const fd = new FormData(); fd.append(field, file);
    if (extra) Object.entries(extra).forEach(([k, v]) => fd.append(k, v));
    return this.http.post<T>(`${this.API_BASE}${endpoint}`, fd);
  }

  download(endpoint: string, id: string): Observable<Blob> { return this.http.get(`${this.API_BASE}${endpoint}/${id}/download`, { responseType: 'blob' }); }

  exportData(endpoint: string, params?: Record<string, string>): Observable<Blob> {
    let hp = new HttpParams();
    if (params) Object.entries(params).forEach(([k, v]) => { hp = hp.set(k, v); });
    return this.http.get(`${this.API_BASE}${endpoint}/export`, { params: hp, responseType: 'blob' });
  }
}
