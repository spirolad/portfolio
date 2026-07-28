import { Component, computed, signal } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('Portfolio backoffice');
  protected readonly token = signal(localStorage.getItem('portfolio-admin-token') ?? '');
  protected readonly hasToken = computed(() => this.token().trim().length > 0);

  protected readonly links = [
    { label: 'Profil', path: '/profile' },
    { label: 'Parcours', path: '/history' },
    { label: 'Projets', path: '/projects' },
    { label: 'Compétences', path: '/skills' }
  ] as const;

  protected updateToken(value: string): void {
    this.token.set(value);
    localStorage.setItem('portfolio-admin-token', value);
  }

  protected clearToken(): void {
    this.updateToken('');
  }
}
