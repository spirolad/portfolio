import { Routes } from '@angular/router';
import { authGuard } from './auth.guard';

export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./backoffice/pages/login/login.page').then((m) => m.LoginPage) },
  { path: '', pathMatch: 'full', redirectTo: 'profile' },
  {
    path: 'profile',
    canActivate: [authGuard],
    loadComponent: () => import('./backoffice/pages/profile.page').then((m) => m.ProfilePage)
  },
  {
    path: 'history',
    canActivate: [authGuard],
    loadComponent: () => import('./backoffice/pages/history.page').then((m) => m.HistoryPage)
  },
  {
    path: 'projects',
    canActivate: [authGuard],
    loadComponent: () => import('./backoffice/pages/projects.page').then((m) => m.ProjectsPage)
  },
  {
    path: 'skills',
    canActivate: [authGuard],
    loadComponent: () => import('./backoffice/pages/skills.page').then((m) => m.SkillsPage)
  },
  { path: '**', redirectTo: 'profile' }
];
