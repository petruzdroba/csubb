import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RaceService } from '../../service/race.service';
import { RaceEvent } from '../../models/race-event';

@Component({
  selector: 'app-race-search',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './race-search.component.html',
})
export class RaceSearchComponent {
  private service = inject(RaceService);
  private cdr = inject(ChangeDetectorRef);

  id: number | null = null;
  engine: number | null = null;

  result: any = null;

  loading = false;

  getById() {
    if (this.id == null) return;

    this.loading = true;
    this.result = null;

    this.service.getById(this.id).subscribe({
      next: (data) => {
        this.result = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  filterByEngine() {
    if (this.engine == null) return;

    this.loading = true;
    this.result = null;

    this.service.filter(this.engine).subscribe({
      next: (data) => {
        this.result = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  clear() {
    this.id = null;
    this.engine = null;
    this.result = null;
  }

  isArrayResult(): boolean {
    return Array.isArray(this.result);
  }

  isObjectResult(): boolean {
    return this.result && !Array.isArray(this.result);
  }
}