import { ChangeDetectorRef, Component, inject, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { AuthService } from "../../service/auth.service";
import { WebSocketService } from "../../service/web-socket.service";
import { GameServie } from "../../service/game.service";
import { Game } from "../../models/game";
import { Configuration } from "../../models/conf";
import { FormsModule } from "@angular/forms";

@Component({
  selector: "app-main-page",
  standalone: true,
  imports: [FormsModule],
  templateUrl: "./main-page.component.html",
})
export class MainPageComponent implements OnInit {
  private auth = inject(AuthService);
  private router = inject(Router);
  private ws = inject(WebSocketService);
  private cdr = inject(ChangeDetectorRef);
  private service = inject(GameServie);

  game: Game | null = null;

  started = false;
  standby = false;
  waiting = false;
  yourTurn = false;

  confs: Configuration[] = [];
  showConf = false;

  ngOnInit() {
    this.ws.onMessage((msg) => {
      console.log("Received message:", msg);

      if (msg == "WAITING") {
        this.waiting = true;
      }

      if (msg == "STANDBY") {
        this.standby = true;
        this.waiting = false;
      }

      if (msg == "START") {
        this.started = true;
        this.standby = false;
      }

      if (msg == "YOUR TURN") {
        this.yourTurn = true;
        if (this.game != null && this.game.configuration == null) {
          this.getGame();
        }
        this.cdr.detectChanges();
      }

      if (msg == "CONFIG") {
        this.config();
      }

      if(msg == "END"){
        this.started = false;
        this.standby = false;
        this.yourTurn = false;
        this.game = null;
        this.cdr.detectChanges();
        alert("Game end");
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

  getGame() {
    this.service.get(this.game!.id).subscribe({
      next: (game) => {
        console.log(game);
        if (game != null) {
          this.game = game;
        }
        this.cdr.detectChanges();
      },
      error: (err) => console.error(err),
    });
  }

  join() {
    this.service.add(this.auth.getUser()!.id).subscribe({
      next: (game) => {
        console.log(game);
        this.game = game;
        this.cdr.detectChanges();
      },
      error: (err) => console.error(err),
    });
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

  config() {
    this.service.getConfs().subscribe((c) => {
      this.confs = c;
      this.showConf = true;
      console.log(c);
      this.cdr.detectChanges();
    });
  }

  setConf(confId: number) {
    this.service.setConf(this.game!.id, confId).subscribe({
      next: () => {
        this.showConf = false;
        this.game!.configuration =
          this.confs.find((c) => c.id === confId) || null;
        this.cdr.detectChanges();
      },
      error: (err) => console.error(err),
    });
  }

  submitGuess(guess: string) {
    this.service
      .action(this.game!.id, this.auth.getUser()!.id, guess)
      .subscribe({
        next: (points) => {
          console.log(`Points received: ${points}`);
          if (this.game) {
            const playerIndex = this.game.players.indexOf(
              this.auth.getUser()!.id,
            );
            if (playerIndex !== -1) {
              this.game.points[playerIndex] += points;
            }
          }
          this.yourTurn = false;
          this.cdr.detectChanges();
        },
        error: (err) => console.error(err),
      });
  }

  addConf(letters: string, word: string) {
    const letters2 = letters.trim().split(/\s+/).map(String);

    this.service.addConf(letters2, word).subscribe({
      next: (conf) => {
        console.log(`Configuration added: ${conf}`);
      },
      error: (err) => console.error(err),
    });
  }
}
