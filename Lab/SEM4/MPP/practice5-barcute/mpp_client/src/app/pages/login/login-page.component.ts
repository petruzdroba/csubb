import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { WebSocketService } from '../../service/web-socket.service';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login-page.component.html',
})
export class LoginPageComponent {
  email = '';
  password = '';
  error = '';
  isRegister = false;
  name = '';

  private auth = inject(AuthService);
  private ws = inject(WebSocketService);
  private router = inject(Router);

  submit() {
    if (this.isRegister) {
      const data = {
        email: this.email,
        password: this.password,
        name: this.name,
      };

      this.auth.register(data).subscribe({
        next: (user) => {
          this.auth.setUser(user);
          this.ws.connect(user.id);
          this.router.navigate(['/']);
        },
        error: () => (this.error = 'Registration failed'),
      });
    } else {
      const data = {
        email: this.email,
        password: this.password,
      };

      this.auth.login(data).subscribe({
        next: (user) => {
          this.auth.setUser(user);
          this.ws.connect(user.id);
          this.router.navigate(['/']);
        },
        error: () => (this.error = 'Invalid credentials'),
      });
    }
  }
}
