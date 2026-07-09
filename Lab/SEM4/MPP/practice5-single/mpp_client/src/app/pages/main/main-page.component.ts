import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { WebSocketService } from '../../service/web-socket.service';
import { GameService } from '../../service/game.service';
import { Game } from '../../models/game';
import { Guess } from '../../models/guess';

@Component({
  selector: 'app-main-page',
  standalone: true,
  templateUrl: './main-page.component.html'
})
export class MainPageComponent implements OnInit {
  private auth = inject(AuthService);
  private router = inject(Router);
  private ws = inject(WebSocketService);
  private cdr = inject(ChangeDetectorRef)
  private service = inject(GameService)

  game: Game| null = null;
  started = false;

  hits: {x:number, y:number}[] = []
  misses: {x:number, y:number}[] = []
  nears: {x:number, y:number}[] = []
  points = 0;


  ngOnInit() {
    this.ws.onMessage((msg) => {
      console.log('Received message:', msg);

      if(msg == "START"){
        this.started = true;
      }

      if(msg == "END"){
        this.started = false;
        this.points = 0;
      }

      this.cdr.detectChanges();
    });
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
    this.service.start(this.auth.getUser()!.id).subscribe({
      next: (game)=>{
        console.log(game);
        this.game = game;
        this.cdr.detectChanges;
      },
      error: (err) => console.error(err)
    })
  }

  guess(X:string, Y:string) {
    this.service.guess(this.auth.getUser()!.id,this.game!.id, parseInt(X), parseInt(Y)).subscribe({
      next: (guess: Guess) => {

        console.log(guess)
        this.points += guess.points
        if(guess.type == 'HIT'){
          this.hits.push({x:parseInt(X), y:parseInt(Y)})
        }

        if(guess.type == 'MISS'){
          this.misses.push({x:parseInt(X), y:parseInt(Y)})
        }

        if(guess.type == 'NEAR'){
          this.nears.push({x:parseInt(X), y:parseInt(Y)})
        }

        this.cdr.detectChanges();
      },
      error: (err) => console.error(err)
    })
  }

  isHit(x:number, y:number){
    return this.hits.some(hit => hit.x == x && hit.y == y)
  }

  isMiss(x:number, y:number){
    return this.misses.some(hit => hit.x == x && hit.y == y)
  }

  isNear(x:number, y:number){
    return this.nears.some(hit => hit.x == x && hit.y == y)
  }
}