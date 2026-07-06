import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RaceErrorService } from '../../service/race-error.service';

@Component({
  selector: 'app-race-error',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './race-error.component.html',
  styleUrl: `./race-error.component.css`
})
export class RaceErrorComponent {
  private errorService = inject(RaceErrorService);

  error = this.errorService.error;

  clear() {
    this.errorService.clear();
  }
}