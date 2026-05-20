import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class RaceErrorService {
  private _error = signal<string | null>(null);

  error = this._error.asReadonly();

  setError(message: string) {
    this._error.set(message);
  }

  clear() {
    this._error.set(null);
  }
}