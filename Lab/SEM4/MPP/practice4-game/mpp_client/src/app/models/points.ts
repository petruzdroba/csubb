export interface Point{
    id:number;
    userId: number;
    gameId: number;
    points: number;
}

export interface Points{
    points: Point[]
}