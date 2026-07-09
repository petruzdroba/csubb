import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Game } from "../models/game";
import { Point, Points } from "../models/points";

@Injectable({providedIn: 'root'})
export class GameService{
    private http = inject(HttpClient)
    private api:string = 'http://localhost:8080/game'

    get(): Observable<Game>{
        return this.http.get<Game>(`${this.api}`)
    }

    start() {
        return this.http.post<void>(`${this.api}/start`, {})
    }

    addPlayer(userId: number){
        return this.http.post<void>(`${this.api}/add`, {userId});
    }

    tom(userId: number, tara:string, oras:string, mare:string):Observable<number>{
        return this.http.post<number>(`${this.api}/tom`, {userId, tara, oras, mare});
    }

    summary(id: number) {
    return this.http.get<Point[]>(`${this.api}/${id}`);
    }
}