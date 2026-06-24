/**
 * Auth API contracts — shared between frontend and backend.
 *
 * <p>Mirrors the Java records in
 * {@code apps/api/src/main/java/in/elcot/avgcxr/platform/auth/api/rest/dto/}.</p>
 */

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;             // → backend fullName
  email: string;
  phone: string;            // → backend phone
  password: string;
  role: string;
  district?: string;
  tamilName?: string;
}

export interface ForgotPasswordRequest {
  email: string;
}

export interface ResetPasswordRequest {
  token: string;
  password: string;
}

export interface RefreshTokenRequest {
  refreshToken: string;     // OR legacy: refresh_token
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: 'Bearer';
  expiresIn: number;        // seconds
  user: import('./user.model').User;
}

export interface UpdateProfileRequest {
  fullName?: string;
  fullNameTamil?: string;
  phone?: string;
  district?: string;
  address?: string;
  pincode?: string;
}
