import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { RaceEvent } from '../models/race-event';
import { RaceErrorService } from './race-error.service';

@Injectable({ providedIn: 'root' })
export class RaceService {
  private http = inject(HttpClient);
  private errorService = inject(RaceErrorService);

  private api = environment.apiUrl + '/races';

  private handleError = (err: any) => {
    const message = err?.error?.error ?? err?.error ?? err?.message ?? 'Unknown error';

    this.errorService.setError(message);

    return throwError(() => err);
  };

  getAll(): Observable<RaceEvent[]> {
    return this.http.get<RaceEvent[]>(this.api).pipe(catchError(this.handleError));
  }

  getById(id: number): Observable<RaceEvent> {
    return this.http.get<RaceEvent>(`${this.api}/${id}`).pipe(catchError(this.handleError));
  }

  filter(engine: number): Observable<RaceEvent[]> {
    return this.http
      .get<RaceEvent[]>(`${this.api}?engine=${engine}`)
      .pipe(catchError(this.handleError));
  }

  create(engine: number): Observable<RaceEvent> {
    return this.http.post<RaceEvent>(this.api, { engine }).pipe(catchError(this.handleError));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`).pipe(catchError(this.handleError));
  }

  update(id: number, engine: number): Observable<RaceEvent> {
    return this.http
      .put<RaceEvent>(`${this.api}/${id}`, { engine })
      .pipe(catchError(this.handleError));
  }
}
