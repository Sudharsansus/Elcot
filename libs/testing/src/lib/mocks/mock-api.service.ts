import { Injectable } from '@angular/core';
import { Observable, of, delay } from 'rxjs';
import { PageResponse } from '@avgc-xr/api-contracts';

@Injectable()
export class MockApiService {
  private delayMs = 200;

  getMockPage<T>(items: T[], total?: number, page = 0, size = 10): PageResponse<T> {
    const start = page * size; const end = start + size;
    return { content: items.slice(start, end), totalElements: total ?? items.length,
      totalPages: Math.ceil((total ?? items.length) / size), currentPage: page, pageSize: size,
      hasNext: end < items.length, hasPrevious: page > 0 };
  }

  success<T>(data: T, d?: number): Observable<T> { return of(data).pipe(delay(d ?? this.delayMs)); }
  error<T>(err: { status: number; message: string }, d?: number): Observable<T> {
    return new Observable<T>(sub => { setTimeout(() => { sub.error(err); sub.complete(); }, d ?? this.delayMs); });
  }

  generateMockSchemes(count: number) {
    const cats = ['SEED_FUNDING','INFRASTRUCTURE_GRANT','SKILL_DEVELOPMENT'];
    return Array.from({ length: count }, (_, i) => ({
      id: `s${i+1}`, name: `Scheme ${i+1}`, nameTamil: `திட்டம் ${i+1}`,
      description: 'Tamil Nadu AVGC industry support scheme.', category: cats[i % 3],
      maxSubsidyAmount: 5000000 + i * 1000000, status: 'OPEN', totalApplications: 150 + i * 30,
    }));
  }

  generateMockApplications(count: number) {
    const statuses = ['SUBMITTED','UNDER_REVIEW','APPROVED','REJECTED'];
    return Array.from({ length: count }, (_, i) => ({
      id: `a${i+1}`, schemeId: `s${(i%5)+1}`, status: statuses[i%4],
      applicationNumber: `AVGC/2026/${String(i+1).padStart(4,'0')}`,
      submissionDate: new Date(2026, i%12, (i%28)+1).toISOString(),
    }));
  }
}
