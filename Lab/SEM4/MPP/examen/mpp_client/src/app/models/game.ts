import { Configuration } from "./conf";

export interface Game {
  id: number;
  players: number[];
  points: number[];
  started: boolean;
  finished: boolean;
  currentTurnIndex: number;
  currentPlayerId: number;
  configuration: Configuration | null
}