import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ClientProfile } from '../models/client-profile.model';

@Injectable({ providedIn: 'root' })
export class ClientProfileService {
  constructor(private http: HttpClient) {}

  getProfile(): Observable<ClientProfile> {
    return this.http.get<ClientProfile>('/api/clients/profile');
  }

  updateProfile(profile: Partial<ClientProfile>): Observable<ClientProfile> {
    return this.http.put<ClientProfile>('/api/clients/profile', profile);
  }
} 