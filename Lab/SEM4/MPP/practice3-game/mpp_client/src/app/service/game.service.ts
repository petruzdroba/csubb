import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Game } from "../models/game";

@Injectable({ providedIn: 'root' })
export class GameService {
    private http = inject(HttpClient);
    private api = 'http://localhost:8080/game';

    getGame() { return this.http.get<Game>(this.api); }
    start() { return this.http.post<Game>(`${this.api}/start`, {}); }
    addPlayer(userId: number, word: string) {
        return this.http.post<void>(`${this.api}/add`, { userId, word });
    }
    guess(userId: number, wordId: number, letter: string) {
        return this.http.post<number>(`${this.api}/guess`, { userId, wordId, letter });
    }
}