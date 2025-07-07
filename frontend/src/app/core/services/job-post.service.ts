import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JobPostRequest, JobPostResponse } from '../models/job-post.model';

@Injectable({ providedIn: 'root' })
export class JobPostService {
  private apiUrl = '/api/jobs';

  constructor(private http: HttpClient) {}

  getAllJobs(): Observable<JobPostResponse[]> {
    return this.http.get<JobPostResponse[]>(`${this.apiUrl}/public`);
  }

  getJob(id: number): Observable<JobPostResponse> {
    return this.http.get<JobPostResponse>(`${this.apiUrl}/${id}`);
  }

  createJob(request: JobPostRequest): Observable<JobPostResponse> {
    return this.http.post<JobPostResponse>(this.apiUrl, request);
  }

  updateJob(id: number, request: JobPostRequest): Observable<JobPostResponse> {
    return this.http.put<JobPostResponse>(`${this.apiUrl}/${id}`, request);
  }

  deleteJob(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getJobsByClient(): Observable<JobPostResponse[]> {
    return this.http.get<JobPostResponse[]>(`${this.apiUrl}/my-jobs`);
  }
} 