import { Tom } from "./tom";

export interface Game{
    id: number;
    players: number[];
    currentLetter: string;
    toms: Tom[];
    turn:number;
}