import { CommonModule } from "@angular/common";
import { Component, EventEmitter, Input, Output } from "@angular/core";
import { Point, Points } from "../../models/points";

@Component({
  selector: "app-list",
  templateUrl: "./list.component.html",
  imports: [CommonModule],
  standalone: true,
})
export class ListComponent {
  @Input() points: Point[] | null = null;
  
  get topUsers() {
    if (!this.points) return [];

    const totals = this.points.reduce(
      (acc, point) => {
        acc[point.userId] = (acc[point.userId] || 0) + point.points;
        return acc;
      },
      {} as Record<number, number>
    );

    return Object.entries(totals)
      .map(([userId, totalPoints]) => ({
        userId: Number(userId),
        totalPoints,
      }))
      .sort((a, b) => b.totalPoints - a.totalPoints)
  }
}