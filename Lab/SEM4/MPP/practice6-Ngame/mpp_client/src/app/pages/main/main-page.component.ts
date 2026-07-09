import { ChangeDetectorRef, Component, inject, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { AuthService } from "../../service/auth.service";
import { WebSocketService } from "../../service/web-socket.service";
import { GameService } from "../../service/game.service";
import { Game } from "../../models/game";
import { FormsModule } from "@angular/forms";
import { Configuration } from "../../models/configuration";

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
  private service = inject(GameService);

  game: Game | null = null;
  configurations: Configuration[] = [];
  showConfigurationPicker = false;

  started = false;
  standby = false;
  waiting = false;
  yourTurn = false;

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
      }

      if (msg == "CONFIG") {
        this.config();
      }

      if (msg == "END") {
        this.game = null;
        this.started = false;
        this.yourTurn = false;
        this.standby = false;
        this.waiting = false;

        alert("Game over!");
      }

      if (this.game) {
        this.service.get(this.game.id).subscribe((game) => {
          this.game = game;
          this.cdr.detectChanges();
        });
      } else {
        this.cdr.detectChanges();
      }
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
        },
        error: (err) => console.error(err),
      });
    }
  }

  config() {
    this.service.getConfigurations().subscribe((configs) => {
      this.configurations = configs;
      this.showConfigurationPicker = true;
    });
  }

  selectConfiguration(configurationId: number) {
    this.service
      .setConfiguration(this.game!.id, configurationId)
      .subscribe(() => {
        this.showConfigurationPicker = false;
        this.cdr.detectChanges();
      });
  }

  action() {
    this.service
      .action(this.game!.id, this.auth.getUser()!.id)
      .subscribe((points) => {
        console.log(`You earned ${points} points.`);

        this.service.get(this.game!.id).subscribe((game) => {
          this.game = game;
        });

        this.yourTurn = false;

        this.cdr.detectChanges();
      });
  }
}
