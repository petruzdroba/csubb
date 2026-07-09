import { ChangeDetectorRef, Component, inject, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { AuthService } from "../../service/auth.service";
import { WebSocketService } from "../../service/web-socket.service";
import { GameService } from "../../service/game.service";
import { Game } from "../../models/game";
import { Word } from "../../models/word";

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
  words: { userId: number; word: Word[] }[] = [];
  started = false;
  yourTurn = false;
  canStart = false;

  ngOnInit() {
    this.ws.onMessage((msg) => {
      console.log(msg);
      if (msg === "GAME START") {
        this.started = true;
        this.canStart = false;
        this.load();
      }
      if (msg === "GAME END") {
        this.started = false;
        this.game = null;
        this.words = [];
      }
      if (msg === "YOUR TURN") {
        this.yourTurn = true;
        this.load();
      }
      if (msg === "CAN START") {
        this.canStart = true;
      }
      if (msg === "CANNOT START") {
        this.canStart = false;
      }
      this.cdr.detectChanges();
    });
  }

  load() {
    this.service.getGame().subscribe({
      next: (game) => {
        this.game = game;
        if (game?.words) {
          const map = new Map<number, Word[]>();
          game.words.forEach((w) => {
            if (!map.has(w.userId)) map.set(w.userId, []);
            map.get(w.userId)!.push(w);
          });
          this.words = Array.from(map.entries()).map(([userId, word]) => ({
            userId,
            word,
          }));
        }
        this.cdr.detectChanges();
      },
    });
  }

  startGame() {
    this.service.start().subscribe({ next: () => this.load() });
  }

  addPlayer(word: string) {
    this.service.addPlayer(this.auth.getUser()!.id, word).subscribe();
  }

  guess(userId: number, wordId: number, letter: string) {
    if (!letter || !this.yourTurn) return;
    this.yourTurn = false;
    this.service.guess(userId, wordId, letter).subscribe({
      next: () => this.load(),
      error: (err) => console.error(err),
    });
  }

  isVowel(l: string) {
    return "aeiou".includes(l.toLowerCase());
  }
  get user() {
    return this.auth.getUser();
  }
  logout() {
    this.auth.logout().subscribe(() => {
      this.ws.disconnect();
      this.router.navigate(["/login"]);
    });
  }
}
