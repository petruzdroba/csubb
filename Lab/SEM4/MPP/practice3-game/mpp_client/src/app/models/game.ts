import { Word } from "./word";

export interface Game{
    id: number;
    players: number[];
    words: Word[];
}