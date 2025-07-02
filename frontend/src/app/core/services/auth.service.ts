import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { User, UserRole } from '../models/user.model';
import { AuthResponse } from '../models/auth-response.model';
import { StorageService } from './storage.service';
import { RegisterRequest } from 'src/app/features/auth/components/register/register.component';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router,
    private storageService: StorageService
  ) {
    // Check if user is already logged in
    this.loadUserFromStorage();
  }

  login(email: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, {
      email,
      password
    }).pipe(
      tap(response => {
        this.setSession(response);
      })
    );
  }

  register(userData: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, userData)
      .pipe(
        tap(response => {
          this.setSession(response);
        })
      );
  }

  logout(): void {
    this.storageService.removeToken();
    this.storageService.removeRefreshToken();
    this.storageService.removeUser();
    this.currentUserSubject.next(null);
    this.router.navigate(['/auth/login']);
  }

  isAuthenticated(): boolean {
    return !!this.storageService.getToken();
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  hasRole(role: UserRole): boolean {
    const user = this.getCurrentUser();
    return user?.role === role;
  }

  private setSession(authResponse: AuthResponse): void {
    this.storageService.setToken(authResponse.token);
    this.storageService.setRefreshToken(authResponse.refreshToken);
    this.storageService.setUser(authResponse.user);
    this.currentUserSubject.next(authResponse.user);
  }

  private loadUserFromStorage(): void {
    const user = this.storageService.getUser();
    if (user && this.isAuthenticated()) {
      this.currentUserSubject.next(user);
    }
  }
}