/**
 * Scheme Domain Models
 */
export interface Scheme {
  id: string; name: string; nameTamil: string; description: string; descriptionTamil: string;
  category: SchemeCategory; subSector: string; maxSubsidyAmount: number; subsidyPercentage: number;
  minProjectCost: number; maxProjectCost: number; startDate: string; endDate: string;
  status: SchemeStatus; guidelinesUrl?: string; applicationFormConfig: FormSection[];
  requiredDocuments: RequiredDocument[]; eligibilityCriteria: EligibilityCriteria;
  faqs: Faq[]; totalApplications?: number; totalDisbursed?: number;
  thumbnail?: string; maxFunding?: number; deadline?: string;
}
export enum SchemeCategory { SEED_FUNDING='SEED_FUNDING', INFRASTRUCTURE_GRANT='INFRASTRUCTURE_GRANT', SKILL_DEVELOPMENT='SKILL_DEVELOPMENT', PRODUCTION_SUBSIDY='PRODUCTION_SUBSIDY', TALENT_INCENTIVE='TALENT_INCENTIVE', MARKETING_SUPPORT='MARKETING_SUPPORT', TAX_REBATE='TAX_REBATE' }
export enum SchemeStatus { DRAFT='DRAFT', PUBLISHED='PUBLISHED', OPEN='OPEN', CLOSED='CLOSED', ARCHIVED='ARCHIVED' }
export interface FormSection { id: string; title: string; titleTamil: string; order: number; fields: FormField[] }
export interface FormField {
  id: string; label: string; labelTamil: string; fieldType: FieldType; required: boolean;
  validation?: ValidationRule[]; options?: SelectOption[]; dependsOn?: string;
  maxLength?: number; minLength?: number; pattern?: string; defaultValue?: string;
}
export enum FieldType { TEXT='TEXT', TEXTAREA='TEXTAREA', NUMBER='NUMBER', DATE='DATE', SELECT='SELECT', MULTI_SELECT='MULTI_SELECT', RADIO='RADIO', CHECKBOX='CHECKBOX', FILE='FILE', EMAIL='EMAIL', PHONE='PHONE', AADHAAR='AADHAAR', PAN='PAN', PINCODE='PINCODE' }
export interface ValidationRule { type: 'REQUIRED'|'MIN_LENGTH'|'MAX_LENGTH'|'PATTERN'|'MIN'|'MAX'|'CUSTOM'; value?: string|number; message: string; messageTamil: string }
export interface SelectOption { value: string; label: string; labelTamil: string }
export interface RequiredDocument { id: string; name: string; nameTamil: string; description: string; descriptionTamil: string; maxSizeKB: number; allowedMimeTypes: string[]; required: boolean; maxCount: number }
export interface EligibilityCriteria { minAge?: number; maxAge?: number; requiredEntityTypes?: string[]; minTurnover?: number; maxTurnover?: number; requiredDistricts?: string[]; requiredSubSectors?: string[]; description: string; descriptionTamil: string }
export interface Faq { id: string; question: string; questionTamil: string; answer: string; answerTamil: string; order: number }
export interface SchemeListRequest { page: number; size: number; category?: SchemeCategory; status?: SchemeStatus; subSector?: string; search?: string; sortBy?: string; sortDirection?: 'ASC'|'DESC' }
