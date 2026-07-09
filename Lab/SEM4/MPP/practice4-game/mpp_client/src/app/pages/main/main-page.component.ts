import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { WebSocketService } from '../../service/web-socket.service';
import { Game } from '../../models/game';
import { GameService } from '../../service/game.service';
import { Point, Points } from '../../models/points';
import { ListComponent } from "../../components/list/list.component";

@Component({
  selector: 'app-main-page',
  standalone: true,
  templateUrl: './main-page.component.html',
  imports: [ListComponent]
})
export class MainPageComponent implements OnInit {
  private auth = inject(AuthService);
  private router = inject(Router);
  private ws = inject(WebSocketService);
  private service = inject(GameService);
  private cdr = inject(ChangeDetectorRef)

  started = false;
  standBy = false; // !waiting
  game: Game | null = null;
  result: Point[] | null = null;
  points:number = 0;

  ngOnInit() {
    this.load();
    this.ws.onMessage((msg) => {
      console.log('Received message:', msg);

      if(msg === "START"){
        this.started = true;
        this.standBy = false;
        this.result = null;
      }

      if(msg === "END"){
        this.summary(this.game!.id);
        this.started = false;
        this.game = null;
      }

      if(msg === "STANDBY"){
        this.standBy = true;
      }

      if(msg === "WAITING"){
        this.standBy = false;
      }

      if(msg === "UPDATE"){
        this.load();
      }

      this.cdr.detectChanges();
    });
  }

  load(){
    this.service.get().subscribe({
      next: (game) =>{
        console.log(game);
        this.game = game;
        this.cdr.markForCheck();
      },
      error: (err) => console.error(err)
    })
    this.cdr.detectChanges();
  }

  logout() {
    this.auth.logout().subscribe({
      next: () => {
        this.ws.disconnect();
        this.router.navigate(['/login']);
      }
    });
  }

  get user() {
    return this.auth.getUser();
  }

  start(){
    this.service.start().subscribe();
  }

  add(){
    this.service.addPlayer(this.auth.getUser()!.id).subscribe();
  }

  submit(tara: string, oras:string, mare:string){
      this.service.tom(this.auth.getUser()!.id, tara, oras, mare).subscribe({
        next: (point) =>{
          this.points += point;
          this.cdr.detectChanges();
        },
        error: (err) => console.error(err)
      })
  }

  summary(id: number){
    this.service.summary(id).subscribe({
      next: (data) => {
          this.result = data;
          this.cdr.detectChanges();
      }
    })
  }
}