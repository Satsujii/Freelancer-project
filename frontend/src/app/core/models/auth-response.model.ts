import { User } from './user.model';

export interface AuthResponse {
  token: string;
  refreshToken?: string;
  expiresIn?: number;
  // Backend fields
  id: number;
  name: string;
  email: string;
  role: string;
  authorities?: string[];
  type?: string | null;
}