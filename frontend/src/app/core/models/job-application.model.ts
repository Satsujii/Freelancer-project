import { ApplicationStatus } from './application-status.enum';

export interface JobApplication {
    id: number;
    jobId: number;
    freelancerId: number;
    freelancerName: string;
    freelancerEmail: string;
    status: ApplicationStatus;
    appliedAt: Date;
} 