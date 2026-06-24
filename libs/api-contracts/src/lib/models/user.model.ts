/**
 * User Domain Models
 */
export interface User {
  id: string; username: string; email: string; mobileNumber: string;
  fullName: string; fullNameTamil?: string; roles: Role[]; status: UserStatus;
  department?: string; designation?: string; district?: string; lastLoginAt?: string;
  createdAt: string; updatedAt: string; profileCompleted: boolean; avatarUrl?: string;
}
export enum Role { SUPER_ADMIN='SUPER_ADMIN', ADMIN='ADMIN', SCHEME_MANAGER='SCHEME_MANAGER', REVIEWER='REVIEWER', APPROVER='APPROVER', APPLICANT='APPLICANT', GRIEVANCE_OFFICER='GRIEVANCE_OFFICER', HELPDESK='HELPDESK', VIEWER='VIEWER' }
export enum UserStatus { ACTIVE='ACTIVE', INACTIVE='INACTIVE', LOCKED='LOCKED', PENDING_VERIFICATION='PENDING_VERIFICATION' }
export interface UserProfile { user: User; aadhaarVerified: boolean; panVerified: boolean; mobileVerified: boolean; emailVerified: boolean; applicationsCount: number; approvedCount: number; rejectedCount: number }
export interface RegistrationRequest { fullName: string; fullNameTamil?: string; email: string; mobileNumber: string; password: string; confirmPassword: string; aadhaarNumber: string; panNumber?: string; dateOfBirth: string; gender: string; district: string; entityType?: string; entityName?: string; agreeTerms: boolean }
export interface LoginResponse { accessToken: string; refreshToken: string; expiresIn: number; tokenType: string; user: User }
export interface PasswordResetRequest { email: string; captchaToken?: string }
export interface PasswordChangeRequest { currentPassword: string; newPassword: string; confirmPassword: string }
export interface UserListRequest { page: number; size: number; role?: Role; status?: UserStatus; district?: string; search?: string; sortBy?: string; sortDirection?: 'ASC'|'DESC' }
