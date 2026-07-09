import { inject, Injectable } from '@angular/core';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class WebSocketService {
  private socket: WebSocket | null = null;
  private listeners: ((msg: string) => void)[] = [];

  connect(userId: number) {
    if (this.socket && this.socket.readyState === WebSocket.OPEN) return;

    this.socket = new WebSocket(`ws://localhost:8080/ws?user=${userId}`);

    this.socket.onopen = () => {
      console.log('WS CONNECTED');
    };

    this.socket.onmessage = (event) => {
      const msg = event.data;
      this.listeners.forEach((cb) => cb(msg));
    };

    this.socket.onclose = () => {
      this.socket = null;
    };
  }

  onMessage(callback: (msg: string) => void) {
    this.listeners.push(callback);
  }

  disconnect() {
    this.socket?.close();
    this.socket = null;
    this.listeners = [];
  }
}
