import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ApiClientService } from './api-client.service';

describe('ApiClientService', () => {
  let service: ApiClientService;
  let httpMock: HttpTestingController;
  beforeEach(() => { TestBed.configureTestingModule({ imports: [HttpClientTestingModule], providers: [ApiClientService] }); service = TestBed.inject(ApiClientService); httpMock = TestBed.inject(HttpTestingController); });
  afterEach(() => httpMock.verify());

  it('should be created', () => expect(service).toBeTruthy());

  it('should perform GET', done => {
    service.get<{ name: string }>('/test').subscribe(d => { expect(d.name).toBe('Test'); done(); });
    const req = httpMock.expectOne('/api/v1/test'); expect(req.request.method).toBe('GET'); req.flush({ name: 'Test' });
  });

  it('should perform GET with params', done => {
    service.get('/test', { page: 1, size: 10 }).subscribe(() => done());
    const req = httpMock.expectOne(r => r.url === '/api/v1/test' && r.params.get('page') === '1'); expect(req.request.method).toBe('GET'); req.flush({});
  });

  it('should perform POST', done => {
    service.post('/test', { name: 'New' }).subscribe(() => done());
    const req = httpMock.expectOne('/api/v1/test'); expect(req.request.method).toBe('POST'); expect(req.request.body).toEqual({ name: 'New' }); req.flush({});
  });

  it('should perform DELETE', done => {
    service.delete('/test', '1').subscribe(() => done());
    const req = httpMock.expectOne('/api/v1/test/1'); expect(req.request.method).toBe('DELETE'); req.flush(null);
  });

  it('should upload file as FormData', done => {
    const file = new File(['content'], 'test.pdf', { type: 'application/pdf' });
    service.upload('/upload', file).subscribe(() => done());
    const req = httpMock.expectOne('/api/v1/upload'); expect(req.request.body instanceof FormData).toBeTrue(); req.flush({});
  });
});
