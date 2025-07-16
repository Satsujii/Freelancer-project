import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';
import { JobPostResponse } from '../models/job-post.model';

export interface JobStatistics {
  [status: string]: number;
}

@Injectable({ providedIn: 'root' })
export class AdminService {
  private apiUrl = '/api/admin';

  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/users`);
  }

  getAllJobs(): Observable<JobPostResponse[]> {
    return this.http.get<JobPostResponse[]>(`${this.apiUrl}/jobs`);
  }

  getJobStatistics(): Observable<JobStatistics> {
    return this.http.get<JobStatistics>(`${this.apiUrl}/statistics`);
  }

  getTotalJobs(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/statistics/total-jobs`);
  }

  updateUser(id: number, user: Partial<User>): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/users/${id}`, user);
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/users/${id}`);
  }

  updateJob(id: number, job: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/jobs/${id}`, job);
  }

  deleteJob(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/jobs/${id}`);
  }
} 