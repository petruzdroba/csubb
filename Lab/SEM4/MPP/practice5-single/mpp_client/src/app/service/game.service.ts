import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { env } from "../../env/env";
import { Game } from "../models/game";
import { Guess } from "../models/guess";


@Injectable({providedIn: 'root'})
export class GameService{
    http = inject(HttpClient)

    get(gameId: number): Observable<Game>{
        return this.http.get<Game>(`${env.apiUrl}/game/${gameId}`);
    }

    start(userId:number):Observable<Game>{
        return this.http.post<Game>(`${env.apiUrl}/game/start`, {userId});
    }

    guess(userId:number,gameId: number, X:number, Y:number){
        return this.http.put<Guess>(`${env.apiUrl}/game/guess`, {
            userId,gameId, X, Y
        })
    }
}