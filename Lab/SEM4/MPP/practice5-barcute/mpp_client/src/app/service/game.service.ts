import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Game } from "../models/game";

@Injectable({ providedIn: "root" })
export class GameService {
  private http = inject(HttpClient);
  private api = "http://localhost:8080/game";

  get(id: number): Observable<Game> {
    return this.http.get<Game>(`${this.api}/${id}`);
  }

  start(id: number): Observable<Game> {
    return this.http.get<Game>(`${this.api}/${id}/start`);
  }

  addPlayer(userId: number, X1:number, Y1: number, X2: number, Y2:number): Observable<Game>{
    return this.http.post<Game>(`${this.api}`, {
        userId,
        X1,
        Y1,
        X2,
        Y2
    })
  }

  addGuess(userId: number, gameId: number, X:number, Y:number):Observable<Boolean>{
    return this.http.put<boolean>(`${this.api}`, {
        userId,
        gameId,
        X,
        Y
    })
  }
}
