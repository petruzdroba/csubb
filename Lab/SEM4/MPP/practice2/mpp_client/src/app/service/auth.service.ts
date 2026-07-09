import { Injectable } from "@angular/core";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Router } from "@angular/router";
import { catchError, of, tap } from "rxjs";
import { LoginRequest } from "../models/auth";
import { User } from "../models/user";
import { env } from "../../env/env";

@Injectable({ providedIn: "root" })
export class AuthService {
  private currentUser: User | null = null;

  constructor(
    private http: HttpClient,
    private router: Router,
  ) {}

  login(data: LoginRequest) {
    return this.http
      .post<User>(`${env.apiUrl}/auth/login`, data, { withCredentials: true })
      .pipe(
        tap((user) => {
          this.currentUser = user;
        }),
      );
  }

  register(data: LoginRequest) {
    return this.http
      .post<User>(`${env.apiUrl}/auth/register`, data, {
        withCredentials: true,
      })
      .pipe(tap((user) => (this.currentUser = user)));
  }

  logout() {
    return this.http
      .post(`${env.apiUrl}/auth/logout`, {}, { withCredentials: true })
      .pipe(
        tap(() => {
          this.currentUser = null;
          this.router.navigate(["/login"]);
        }),
      );
  }

  getUser(): User | null {
    return this.currentUser;
  }

  setUser(user: User) {
    this.currentUser = user;
  }

  isLoggedIn(): boolean {
    return this.currentUser !== null;
  }

  loadSessionUser() {
    return this.http
      .get<User>(`${env.apiUrl}/auth/me`, { withCredentials: true })
      .pipe(
        tap((user) => (this.currentUser = user)),

        catchError((err: HttpErrorResponse) => {
          if (err.status === 401) {
            console.log("no session");
            this.currentUser = null;

            return of(null);
          }

          throw err;
        }),
      );
  }
}
