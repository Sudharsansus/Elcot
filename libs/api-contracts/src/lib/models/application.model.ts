/**
 * Application Domain Models
 */
export interface Application {
  id: string; schemeId: string; applicantId: string; status: ApplicationStatus;
  submissionDate: string; lastModifiedDate: string; currentStep: number; totalSteps: number;
  assignedTo?: string; remarks?: string; rejectionReason?: string;
  approvedAmount?: number; applicationNumber: string;
}
export enum ApplicationStatus {
  DRAFT='DRAFT', SUBMITTED='SUBMITTED', UNDER_REVIEW='UNDER_REVIEW',
  ADDITIONAL_INFO_REQUESTED='ADDITIONAL_INFO_REQUESTED', APPROVED='APPROVED',
  REJECTED='REJECTED', DISBURSED='DISBURSED', WITHDRAWN='WITHDRAWN',
}
export interface ApplicationFormData {
  personalDetails: PersonalDetails; businessDetails?: BusinessDetails;
  projectDetails: ProjectDetails; financialDetails: FinancialDetails;
  supportingDocuments: DocumentUpload[];
}
export interface PersonalDetails {
  fullName: string; fullNameTamil?: string; dateOfBirth: string; gender: Gender;
  aadhaarNumber: string; panNumber?: string; mobileNumber: string; email: string;
  address: Address; district: string; taluk: string;
}
export interface Address { line1: string; line2?: string; city: string; district: string; state: string; pincode: string; }
export interface BusinessDetails {
  entityName: string; entityNameTamil?: string; registrationNumber: string;
  gstNumber?: string; cinNumber?: string; entityType: EntityType;
  dateOfIncorporation: string; turnover: number; employeeCount: number;
  udyamRegistrationNumber?: string;
}
export enum EntityType { SOLE_PROPRIETORSHIP='SOLE_PROPRIETORSHIP', PARTNERSHIP='PARTNERSHIP', LLP='LLP', PRIVATE_LIMITED='PRIVATE_LIMITED', PUBLIC_LIMITED='PUBLIC_LIMITED' }
export interface ProjectDetails {
  projectName: string; projectNameTamil?: string; projectDescription: string;
  projectDescriptionTamil?: string; subSector: SubSector; estimatedCost: number;
  requestedSubsidy: number; projectDuration: number; location: string; district: string;
}
export enum SubSector { ANIMATION='ANIMATION', VFX='VFX', GAMING='GAMING', COMICS='COMICS', XR='XR', POST_PRODUCTION='POST_PRODUCTION', DIGITAL_MARKETING='DIGITAL_MARKETING' }
export interface FinancialDetails {
  bankAccountNumber: string; bankName: string; branchName: string;
  ifscCode: string; accountHolderName: string; accountType: AccountType;
}
export enum AccountType { SAVINGS='SAVINGS', CURRENT='CURRENT' }
export enum Gender { MALE='MALE', FEMALE='FEMALE', TRANSGENDER='TRANSGENDER' }
export interface DocumentUpload {
  id: string; documentTypeId: string; fileName: string; fileSize: number;
  mimeType: string; uploadDate: string; status: DocumentStatus; minioObjectId?: string;
}
export enum DocumentStatus { PENDING='PENDING', UPLOADED='UPLOADED', VERIFIED='VERIFIED', REJECTED='REJECTED' }
export interface ApplicationListRequest {
  page: number; size: number; sortBy?: string; sortDirection?: 'ASC'|'DESC';
  status?: ApplicationStatus; schemeId?: string; search?: string; district?: string;
  dateFrom?: string; dateTo?: string;
}
export interface PageResponse<T> {
  content: T[]; totalElements: number; totalPages: number; currentPage: number;
  pageSize: number; hasNext: boolean; hasPrevious: boolean;
}
