import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageResponse } from '@avgc-xr/api-contracts';

@Injectable({ providedIn: 'root' })
export class ApiClientService {
  private readonly http = inject(HttpClient);
  private readonly API_BASE = '/api/v1';

  get<T>(endpoint: string, params?: Record<string, string | number | boolean>): Observable<T> {
    let hp = new HttpParams();
    if (params) Object.entries(params).forEach(([k, v]) => { if (v != null) hp = hp.set(k, String(v)); });
    return this.http.get<T>(`${this.API_BASE}${endpoint}`, { params: hp });
  }

  getPage<T>(endpoint: string, page: number, size: number, extra?: Record<string, string>): Observable<PageResponse<T>> {
    return this.get<PageResponse<T>>(endpoint, { page, size, ...extra });
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
