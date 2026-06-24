import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { Role, UserStatus } from '@avgc-xr/api-contracts';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  const mockResponse = {
    accessToken: 'eyJhbGciOiJIUzI1NiJ9.mock', refreshToken: 'rt-mock',
    expiresIn: 1800, tokenType: 'Bearer',
    user: { id:'u1', username:'test@test.com', email:'test@test.com', mobileNumber:'9876543210',
      fullName:'Test User', roles:[Role.APPLICANT], status:UserStatus.ACTIVE,
      createdAt:'2026-01-01T00:00:00Z', updatedAt:'2026-01-01T00:00:00Z', profileCompleted:true }
  };

  beforeEach(() => { TestBed.configureTestingModule({ imports: [HttpClientTestingModule], providers: [AuthService] }); service = TestBed.inject(AuthService); httpMock = TestBed.inject(HttpTestingController); sessionStorage.clear(); });
  afterEach(() => { httpMock.verify(); sessionStorage.clear(); });

  it('should be created', () => expect(service).toBeTruthy());

  it('should login and store tokens', done => {
    service.login({ username: 'test@test.com', password: 'Pass@1234' }).subscribe({ next: r => { expect(r.user.fullName).toBe('Test User'); expect(service.currentUser).toBeTruthy(); done(); } });
    const req = httpMock.expectOne('/api/v1/auth/login'); expect(req.request.method).toBe('POST'); req.flush(mockResponse);
  });

  it('should clear session on logout', done => {
    service.login({ username: 'test@test.com', password: 'Pass@1234' }).subscribe({ complete: () => {
      service.logout().subscribe({ complete: () => { expect(service.currentUser).toBeNull(); done(); } });
      const lr = httpMock.expectOne('/api/v1/auth/logout'); lr.flush({});
    }});
    const lr = httpMock.expectOne('/api/v1/auth/login'); lr.flush(mockResponse);
  });
});
