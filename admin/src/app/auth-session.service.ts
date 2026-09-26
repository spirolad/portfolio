import { Injectable, OnDestroy, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';

export const AUTH_TOKEN_KEY = 'portfolio-admin-token';

export interface JwtPayload {
  exp?: number;
  iat?: number;
  sub?: string;
  groups?: string[];
  [key: string]: unknown;
}

export function decodeJwt(token: string): JwtPayload | null {
  try {
    const parts = token.split('.');
    if (parts.length !== 3) {
      return null;
    }
    let base64 = parts[1].replace(/-/g, '+').replace(/_/g, '/');
    switch (base64.length % 4) {
      case 0:
        break;
      case 2:
        base64 += '==';
        break;
      case 3:
        base64 += '=';
        break;
      default:
        return null;
    }
    const decodedString = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );
    return JSON.parse(decodedString) as JwtPayload;
  } catch {
    return null;
  }
}

export function isTokenExpired(token: string | null | undefined, offsetSeconds: number = 0): boolean {
  if (!token || typeof token !== 'string' || token.trim() === '') {
    return true;
  }
  const payload = decodeJwt(token);
  if (!payload) {
    return true;
  }
  if (typeof payload.exp !== 'number') {
    return false;
  }
  const currentTime = Math.floor(Date.now() / 1000);
  return payload.exp <= currentTime + offsetSeconds;
}

export function getTokenTimeRemainingMs(token: string | null | undefined): number {
  if (!token) return 0;
  const payload = decodeJwt(token);
  if (!payload || typeof payload.exp !== 'number') return 0;
  const remainingSeconds = payload.exp - Math.floor(Date.now() / 1000);
  return remainingSeconds > 0 ? remainingSeconds * 1000 : 0;
}

@Injectable({
  providedIn: 'root'
})
export class AuthSessionService implements OnDestroy {
  private readonly router = inject(Router);
  private timerId: ReturnType<typeof setTimeout> | null = null;

  private readonly _token = signal<string | null>(this.getStoredToken());
  public readonly token = this._token.asReadonly();
  public readonly isAuthenticated = computed(() => {
    const currentToken = this._token();
    return !!currentToken && !isTokenExpired(currentToken);
  });

  constructor() {
    this.initSession();
  }

  public getStoredToken(): string | null {
    return localStorage.getItem(AUTH_TOKEN_KEY);
  }

  public getToken(): string | null {
    return this._token();
  }

  public isExpired(): boolean {
    const currentToken = this.getToken() ?? this.getStoredToken();
    return isTokenExpired(currentToken);
  }

  public setToken(newToken: string): void {
    localStorage.setItem(AUTH_TOKEN_KEY, newToken);
    this._token.set(newToken);
    this.scheduleExpiration(newToken);
  }

  public clearToken(): void {
    this.cancelExpirationTimer();
    localStorage.removeItem(AUTH_TOKEN_KEY);
    this._token.set(null);
  }

  public logout(): void {
    this.clearToken();
    void this.router.navigate(['/login']);
  }

  private initSession(): void {
    const stored = this.getStoredToken();
    if (!stored) {
      return;
    }

    if (isTokenExpired(stored)) {
      this.clearToken();
    } else {
      this.scheduleExpiration(stored);
    }
  }

  private scheduleExpiration(token: string): void {
    this.cancelExpirationTimer();

    const remainingMs = getTokenTimeRemainingMs(token);
    if (remainingMs <= 0) {
      this.logout();
      return;
    }

    // Maximum timeout value supported in 32-bit int (~24.8 days)
    const safeTimeout = Math.min(remainingMs, 2147483647);
    this.timerId = setTimeout(() => {
      this.logout();
    }, safeTimeout);
  }

  private cancelExpirationTimer(): void {
    if (this.timerId !== null) {
      clearTimeout(this.timerId);
      this.timerId = null;
    }
  }

  ngOnDestroy(): void {
    this.cancelExpirationTimer();
  }
}
