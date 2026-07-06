import { Routes } from '@angular/router';
import { RacePageComponent } from './page/race-page/race-page.component';
import { LoginPageComponent } from './page/login-page/login-page.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginPageComponent },
  { path: '', redirectTo: 'races', pathMatch: 'full' },
  { path: 'races', component: RacePageComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: 'races' }
]