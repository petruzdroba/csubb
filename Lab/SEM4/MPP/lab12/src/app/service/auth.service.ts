import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { AuthResponse, LoginRequest } from '../models/auth';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly TOKEN_KEY = 'token';

  constructor(private http: HttpClient, private router: Router) {}

  login(data: LoginRequest) {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/login`, data).pipe(
      tap(res => localStorage.setItem(this.TOKEN_KEY, res.token))
    );
  }

  register(data: LoginRequest) {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/register`, data).pipe(
      tap(res => localStorage.setItem(this.TOKEN_KEY, res.token))
    );
  }

  logout() {
    localStorage.removeItem(this.TOKEN_KEY);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}