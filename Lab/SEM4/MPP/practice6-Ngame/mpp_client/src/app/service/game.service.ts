import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { env } from "../../env/env";
import { Observable } from "rxjs";
import { Game } from "../models/game";
import { Configuration } from "../models/configuration";

@Injectable({ providedIn: "root" })
export class GameService {
  http = inject(HttpClient);

  api = env.apiUrl + "/game";

  get(id: number): Observable<Game> {
    return this.http.get<Game>(`${this.api}/${id}`);
  }

  add(userId: number): Observable<Game> {
    return this.http.post<Game>(`${this.api}/add`, { userId });
  }

  start(gameId: number): Observable<Game> {
    return this.http.post<Game>(`${this.api}/start`, { gameId });
  }

  action(gameId: number, userId: number): Observable<number> {
    return this.http.post<number>(`${this.api}/action`, { gameId, userId });
  }

  setConfiguration(gameId: number, configurationId: number): Observable<void> {
    return this.http.post<void>(`${this.api}/configuration`, {
      gameId,
      configurationId,
    });
  }

  getConfiguration(gameId: number): Observable<Configuration> {
    return this.http.get<Configuration>(`${this.api}/${gameId}/configuration`);
  }

  getConfigurations(): Observable<Configuration[]> {
    return this.http.get<Configuration[]>(`${this.api}/configuration`);
  }
}
