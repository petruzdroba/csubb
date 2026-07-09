import { ChangeDetectorRef, Component, inject, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { AuthService } from "../../service/auth.service";
import { WebSocketService } from "../../service/web-socket.service";
import { GameService } from "../../service/game.service";
import { Game } from "../../models/game";

@Component({
  selector: "app-main-page",
  standalone: true,
  templateUrl: "./main-page.component.html",
})
export class MainPageComponent implements OnInit {
  auth = inject(AuthService);
  private router = inject(Router);
  private ws = inject(WebSocketService);
  private service = inject(GameService);
  private cdr = inject(ChangeDetectorRef);

  game: Game | null = null;

  started: boolean = false;
  waiting: boolean = false;
  ready: boolean = false;
  turn: boolean = false;

  hits: {x:number, y:number}[] = []
  misses : {x:number, y:number}[] = []

  ngOnInit() {
    this.load();
    this.ws.onMessage((msg) => {
      console.log("Received message:", msg);

      if (msg == "WAITING") {
        this.waiting = true;
        this.cdr.detectChanges();
      }

      if (msg == "READY") {
        this.waiting = false;
        this.ready = true;
        this.cdr.detectChanges();
      }

      if (msg == "START") {
        this.waiting = false;
        this.ready = false;
        this.started = true;
        this.cdr.detectChanges();
      }

      if (msg == "YOUR TURN") {
        this.turn = true;
        this.cdr.detectChanges();
      }

      if(msg == "END"){
        this.waiting = false;
        this.ready = false;
        this.started = false;
        this.game = null;
        this.cdr.detectChanges();
      }

      if(msg == "WON"){
        console.log("YOU WON !")
      }

      this.cdr.detectChanges();
    });
  }

  logout() {
    this.auth.logout().subscribe({
      next: () => {
        this.ws.disconnect();
        this.router.navigate(["/login"]);
      },
    });
  }

  get user() {
    return this.auth.getUser();
  }

  load() {
    if (this.game != null) {
      this.service.get(this.game.id).subscribe({
        next: (game) => {
          console.log(game);
          this.game = game;
          this.cdr.detectChanges();
        },

        error: (err) => console.error(err),
      });
    }
  }

  start() {
    if (this.game != null) {
      this.service.start(this.game.id).subscribe({
        next: (game) => {
          console.log(game);
          this.game = game;
          this.cdr.detectChanges();
        },

        error: (err) => console.error(err),
      });
    }
  }

  join(X1: string, Y1: string, X2: string, Y2: string) {
    this.service
      .addPlayer(
        this.auth.getUser()!.id,
        parseInt(X1),
        parseInt(Y1),
        parseInt(X2),
        parseInt(Y2),
      )
      .subscribe({
        next: (game) => {
          console.log(game);
          this.game = game;
          this.cdr.detectChanges();
        },

        error: (err) => console.error(err),
      });
  }

  guess(X:string, Y:string) {
    if(this.game != null) {

      this.turn = false;
      this.cdr.detectChanges(); 

      this.service.addGuess(this.auth.getUser()!.id, this.game.id, parseInt(X), parseInt(Y))
      .subscribe({
        next: (data) => {
          if(data == true)
            this.hits.push({x: parseInt(X), y:parseInt(Y)});

          if(data == false) 
            this.misses.push({x: parseInt(X), y:parseInt(Y)});

          this.cdr.detectChanges();
        },
        error: (err) => console.error(err)
      })
    }
  }

   isHit(x: number, y: number): boolean {
    return this.hits.some(hit => hit.x === x && hit.y === y);
  }

  isMiss(x: number, y: number): boolean {
    return this.misses.some(miss => miss.x === x && miss.y === y);
  }
}
