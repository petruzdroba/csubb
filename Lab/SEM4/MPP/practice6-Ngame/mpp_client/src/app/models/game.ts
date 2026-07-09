import { Configuration } from "./configuration";

export interface Game {
  id: number;
  players: number[];
  points: number[];
  positions: number[];
  totalMoves: number;
  configuration: Configuration | null;
  started: boolean;
  finished: boolean;
  currentTurnIndex: number;
  currentPlayerId: number;
}