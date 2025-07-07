import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FreelancerProfile } from '../models/freelancer-profile.model';

@Injectable({ providedIn: 'root' })
export class FreelancerProfileService {
  constructor(private http: HttpClient) {}

  getProfile(): Observable<FreelancerProfile> {
    return this.http.get<FreelancerProfile>('/api/freelancer/profile');
  }

  updateProfile(profile: Partial<FreelancerProfile>): Observable<FreelancerProfile> {
    return this.http.put<FreelancerProfile>('/api/freelancer-profiles', profile);
  }
} 