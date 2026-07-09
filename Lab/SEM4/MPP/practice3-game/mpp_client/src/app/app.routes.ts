import { Routes } from '@angular/router';
import { LoginPageComponent } from './pages/login/login-page.component';
import { MainPageComponent } from './pages/main/main-page.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginPageComponent },
  { path: '', redirectTo: 'main', pathMatch: 'full' },
  { path: 'main', component: MainPageComponent, canActivate: [authGuard] },
]