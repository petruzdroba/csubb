import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class WebSocketService {
  private socket: WebSocket | null = null;
  private onMessageCallback: (() => void) | null = null;

  constructor(private auth: AuthService) {}

  connect(onMessage: () => void) {
    const token = this.auth.getToken();
    if (!token) return;

    this.onMessageCallback = onMessage;
    this.socket = new WebSocket(`ws://localhost:8080/ws/races?token=${token}`);

    this.socket.onmessage = () => {
      if (this.onMessageCallback) this.onMessageCallback();
    };

    this.socket.onerror = (e) => console.error('WebSocket error', e);
  }

  disconnect() {
    this.socket?.close();
    this.socket = null;
  }
}