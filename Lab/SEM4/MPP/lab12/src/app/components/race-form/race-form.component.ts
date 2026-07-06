import {
  Component,
  EventEmitter,
  Input,
  Output,
  OnChanges
} from '@angular/core';

import { FormsModule } from '@angular/forms';
import { RaceEvent } from '../../models/race-event';

@Component({
  selector: 'app-race-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './race-form.component.html'
})
export class RaceFormComponent implements OnChanges {

  @Input() selectedRace: RaceEvent | null = null;
  @Output() saveEvent = new EventEmitter<number>();
  @Output() cancel = new EventEmitter<void>();

  engine = 0;

  ngOnChanges() {
    if (this.selectedRace) {
      this.engine = this.selectedRace.engine;
    }
  }

  submit() {
    this.saveEvent.emit(this.engine);
  }
  
  onCancel() {
    this.engine = 0;
  }
}