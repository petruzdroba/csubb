import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AuthService } from './service/auth.service';
import { WebSocketService } from './service/web-socket.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  private auth = inject(AuthService);
  private ws = inject(WebSocketService);

  ngOnInit() {
    this.auth.loadSessionUser().subscribe({
      next: (user) => {
        if (user) {
          this.ws.connect(user.id);
        }
      },
      error: () => {},
    });
  }

  protected readonly title = signal('mpp');
}
