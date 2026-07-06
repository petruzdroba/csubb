import { ChangeDetectorRef, Component, OnDestroy, OnInit, inject } from '@angular/core';
import { RaceEvent } from '../../models/race-event';
import { RaceListComponent } from '../../components/race-list/race-list.component';
import { RaceFormComponent } from '../../components/race-form/race-form.component';
import { RaceService } from '../../service/race.service';
import { RaceErrorComponent } from '../../components/race-error/race-error.component';
import { RaceSearchComponent } from '../../components/race-search/race-search.component';
import { AuthService } from '../../service/auth.service';
import { WebSocketService } from '../../service/websocket.service';

@Component({
  selector: 'app-race-page',
  standalone: true,
  imports: [RaceListComponent, RaceFormComponent, RaceErrorComponent, RaceSearchComponent],
  templateUrl: './race-page.component.html',
})
export class RacePageComponent implements OnInit, OnDestroy {
  private service = inject(RaceService);
  private cdr = inject(ChangeDetectorRef);
  private auth = inject(AuthService);
  private ws = inject(WebSocketService);

  races: RaceEvent[] = [];
  selectedRace: RaceEvent | null = null;
  isEditing: boolean = false;

  ngOnInit() {
    this.load();
    this.ws.connect(() => {
      this.load();
    });
  }

  ngOnDestroy() {
    this.ws.disconnect();
  }

  load() {
    this.service.getAll().subscribe((data) => {
      this.races = [...data];
      this.cdr.detectChanges();
    });
  }

  save(engine: number) {
    if (this.selectedRace) {
      this.service.update(this.selectedRace.id, engine).subscribe(() => {
        this.selectedRace = null;
        this.isEditing = false;
        this.load();
      });
    } else {
      this.service.create(engine).subscribe(() => {
        this.load();
      });
    }
  }

  edit(race: RaceEvent) {
    this.selectedRace = race;
    this.isEditing = true;
  }

  delete(id: number) {
    this.service.delete(id).subscribe(() => {
      this.load();
    });
  }

  cancelEdit() {
    this.selectedRace = null;
    this.isEditing = false;
  }

  logout() {
    this.ws.disconnect();
    this.auth.logout();
  }
}