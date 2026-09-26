import { Component, inject, signal } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthSessionService } from './auth-session.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  private readonly authSession = inject(AuthSessionService);
  protected readonly title = signal('Portfolio backoffice');
  protected readonly token = this.authSession.token;
  protected readonly hasToken = this.authSession.isAuthenticated;

  protected readonly links = [
    { label: 'Profil', path: '/profile' },
    { label: 'Parcours', path: '/history' },
    { label: 'Projets', path: '/projects' },
    { label: 'Compétences', path: '/skills' }
  ] as const;

  protected updateToken(value: string): void {
    if (value) {
      this.authSession.setToken(value);
    } else {
      this.authSession.clearToken();
    }
  }

  protected clearToken(): void {
    this.authSession.logout();
  }
}
