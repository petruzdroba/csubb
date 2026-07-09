import { inject, Injectable } from "@angular/core";
import { Participant } from "../models/particiapant";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";

@Injectable({ providedIn: "root" })
export class ParticipantService{

    private api = "http://localhost:8080";
    private http = inject(HttpClient);

    getAll(): Observable<Participant[]> {
        return this.http.get<Participant[]>(this.api);  
    }


    ready(id:number): Observable<Participant> {
        return this.http.post<Participant>(`${this.api}/ready`, {id});
    }

    score(id:number, score:number, judge: number):Observable<Participant>{
        return this.http.post<Participant>(`${this.api}`, {id, score, judge});
    }
}