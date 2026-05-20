import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login-page.component.html'
})
export class LoginPageComponent {
  email = '';
  password = '';
  error = '';
  isRegister = false;

  constructor(private auth: AuthService, private router: Router) {}

  submit() {
    const data = { email: this.email, password: this.password };
    const call = this.isRegister ? this.auth.register(data) : this.auth.login(data);
    call.subscribe({
      next: () => this.router.navigate(['/races']),
      error: () => this.error = 'Invalid credentials'
    });
  }
}