import { Injectable, signal } from '@angular/core';
import { Observable, of } from 'rxjs';
import { User, Role, UserStatus, LoginResponse } from '@avgc-xr/api-contracts';

@Injectable()
export class MockAuthService {
  private users: Record<string, User> = {
    admin: { id:'ma1', username:'admin@elcot.tn.gov.in', email:'admin@elcot.tn.gov.in', mobileNumber:'9440010001', fullName:'System Administrator', fullNameTamil:'அமைப்பு நிர்வாகி', roles:[Role.SUPER_ADMIN,Role.ADMIN], status:UserStatus.ACTIVE, department:'ELCOT', createdAt:'2026-01-01T00:00:00Z', updatedAt:'2026-06-23T00:00:00Z', profileCompleted:true },
    applicant: { id:'ma2', username:'test@test.com', email:'test@test.com', mobileNumber:'9876543210', fullName:'Test Applicant', fullNameTamil:'பரிசோதனை விண்ணப்பதாரர்', roles:[Role.APPLICANT], status:UserStatus.ACTIVE, createdAt:'2026-03-15T00:00:00Z', updatedAt:'2026-06-23T00:00:00Z', profileCompleted:true },
    reviewer: { id:'ma3', username:'rev@elcot.tn.gov.in', email:'rev@elcot.tn.gov.in', mobileNumber:'9440010002', fullName:'Scheme Reviewer', fullNameTamil:'திட்ட விமர்சகர்', roles:[Role.REVIEWER,Role.APPROVER], status:UserStatus.ACTIVE, department:'ELCOT', createdAt:'2026-02-01T00:00:00Z', updatedAt:'2026-06-23T00:00:00Z', profileCompleted:true },
  };

  getMockUser(key: string): User { return this.users[key] ?? this.users.applicant; }

  getMockLoginResponse(key: string): LoginResponse {
    const user = this.getMockUser(key);
    return { accessToken: 'mock-jwt-access', refreshToken: 'mock-jwt-refresh', expiresIn: 1800, tokenType: 'Bearer', user };
  }

  loginAs(role: 'admin' | 'applicant' | 'reviewer'): Observable<LoginResponse> { return of(this.getMockLoginResponse(role)); }
  getUser(): User { return this.users.applicant; }
  isLoggedIn = signal(false);
  setIsLoggedIn(val: boolean): void { this.isLoggedIn.set(val); }
}
