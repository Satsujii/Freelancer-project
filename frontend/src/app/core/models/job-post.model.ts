export interface JobPostRequest {
  title: string;
  description: string;
  deadline: string; // ISO date string
  budget: number;
}

export type JobStatus = 'OPEN' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';

export interface JobPostResponse {
  id: number;
  title: string;
  description: string;
  budget: number;
  deadline: string;
  status: JobStatus;
  createdAt: string;
  updatedAt: string;
  clientId: number;
  clientName: string;
  clientEmail: string;
  clientCompanyName: string;
} 