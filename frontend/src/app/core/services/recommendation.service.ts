import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JobPostResponse } from '../models/job-post.model';

@Injectable({ providedIn: 'root' })
export class RecommendationService {
  private baseUrl = '/api/recommendations';

  constructor(private http: HttpClient) {}

  getRecommendations(userId: number): Observable<JobPostResponse[]> {
    return this.http.get<JobPostResponse[]>(`${this.baseUrl}/${userId}`);
  }
} 