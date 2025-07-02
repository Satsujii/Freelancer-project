export interface User {
  id: number;
  name: string;
  email: string;
  role: UserRole;
  enabled: boolean;
  createdAt: Date;
  updatedAt: Date;
}

export enum UserRole {
  ADMIN = 'ADMIN',
  FREELANCER = 'FREELANCER',
  CLIENT = 'CLIENT'
}