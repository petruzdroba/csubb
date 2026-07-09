import { CommonModule } from "@angular/common";
import { Component, EventEmitter, Input, Output } from "@angular/core";
import { Participant } from "../../models/particiapant";

@Component({
  selector: "app-list",
  templateUrl: "./list.component.html",
  imports: [CommonModule],
  standalone: true,
})
export class ListComponent {
  @Input() participants: Participant[] = [];
  @Input() status: string = "";

  @Output() readyEvent = new EventEmitter<Participant>();

  @Output() editEvent = new EventEmitter<{
    participant: Participant;
    score: number;
  }>();


  onEdit(participant: Participant, score: string) {
    const numScore = parseInt(score, 10);
    this.editEvent.emit({ participant, score: numScore });
  }

  onReady(participant: Participant) {
    this.readyEvent.emit(participant);
  }
}
