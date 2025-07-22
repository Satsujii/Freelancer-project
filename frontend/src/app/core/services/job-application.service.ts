import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JobApplication } from '../models/job-application.model';

@Injectable({
  providedIn: 'root'
})
export class JobApplicationService {
  private apiUrl = '/api/applications';

  constructor(private http: HttpClient) { }

  applyForJob(jobId: number): Observable<JobApplication> {
    return this.http.post<JobApplication>(`${this.apiUrl}/apply/${jobId}`, {});
  }

  getApplicationsForJob(jobId: number): Observable<JobApplication[]> {
    return this.http.get<JobApplication[]>(`${this.apiUrl}/job/${jobId}`);
  }

  getApplicationsForFreelancer(): Observable<JobApplication[]> {
    return this.http.get<JobApplication[]>(`/api/freelancer/applications`);
  }

  acceptApplication(applicationId: number): Observable<JobApplication> {
    return this.http.post<JobApplication>(`${this.apiUrl}/accept/${applicationId}`, {});
  }

  rejectApplication(applicationId: number): Observable<JobApplication> {
    return this.http.post<JobApplication>(`${this.apiUrl}/reject/${applicationId}`, {});
  }
} 