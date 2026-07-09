import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { env } from "../../env/env";
import { Observable } from "rxjs";
import { Game } from "../models/game";
import { Configuration } from "../models/conf";

@Injectable({ providedIn: "root" })
export class GameServie {
  http = inject(HttpClient);

  api = env.apiUrl + "/game";

  get(id: number): Observable<Game> {
    return this.http.get<Game>(`${this.api}/${id}`);
  }

  add(userId: number) {
    return this.http.post<Game>(`${this.api}/add`, { userId });
  }

  start(gameId: number) {
    return this.http.post<Game>(`${this.api}/start`, { gameId });
  }

  action(gameId: number, userId: number, word:string): Observable<number> {
    return this.http.post<number>(`${this.api}/action`, { gameId, userId, word });
  }

  getConfs() : Observable<Configuration[]> {
    return this.http.get<Configuration[]>(`${env.apiUrl}/config`);
  }

  setConf(gameId: number, confId: number): Observable<void> {
    return this.http.post<void>(`${this.api}/config`, { gameId, confId });
  }

  addConf(letters: string[], word: string): Observable<Configuration> {
    return this.http.post<Configuration>(`${env.apiUrl}/config`, { letters, word });
  }
}
