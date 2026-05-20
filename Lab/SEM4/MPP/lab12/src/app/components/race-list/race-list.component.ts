import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RaceEvent } from '../../models/race-event';

@Component({
  selector: 'app-race-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './race-list.component.html'
})
export class RaceListComponent {

  @Input() races: RaceEvent[] = [];

  @Output() deleteEvent = new EventEmitter<number>();

  @Output() editEvent = new EventEmitter<RaceEvent>();

  delete(id: number) {
    this.deleteEvent.emit(id);
  }

  edit(race: RaceEvent) {
    this.editEvent.emit(race);
  }
}