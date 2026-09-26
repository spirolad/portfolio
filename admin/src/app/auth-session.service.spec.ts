import { TestBed, fakeAsync, tick } from '@angular/core/testing';
import { Router } from '@angular/router';
import {
  AUTH_TOKEN_KEY,
  AuthSessionService,
  decodeJwt,
  getTokenTimeRemainingMs,
  isTokenExpired
} from './auth-session.service';

function createMockJwt(expOffsetSeconds: number): string {
  const header = btoa(JSON.stringify({ alg: 'RS256', typ: 'JWT' }))
    .replace(/=/g, '')
    .replace(/\+/g, '-')
    .replace(/\//g, '_');
  const exp = Math.floor(Date.now() / 1000) + expOffsetSeconds;
  const payload = btoa(JSON.stringify({ sub: 'admin', exp, groups: ['admin'] }))
    .replace(/=/g, '')
    .replace(/\+/g, '-')
    .replace(/\//g, '_');
  return `${header}.${payload}.signature`;
}

describe('AuthSessionService & JWT utils', () => {
  let routerSpy: { navigate: ReturnType<typeof vi.fn> };

  beforeEach(() => {
    localStorage.clear();
    routerSpy = { navigate: vi.fn().mockResolvedValue(true) };
  });

  afterEach(() => {
    localStorage.clear();
  });

  describe('JWT utils', () => {
    it('should decode a valid JWT payload', () => {
      const token = createMockJwt(3600);
      const decoded = decodeJwt(token);
      expect(decoded).not.toBeNull();
      expect(decoded?.sub).toBe('admin');
      expect(decoded?.groups).toEqual(['admin']);
    });

    it('should return null for malformed JWT', () => {
      expect(decodeJwt('not-a-jwt')).toBeNull();
      expect(decodeJwt('part1.part2')).toBeNull();
      expect(decodeJwt('')).toBeNull();
    });

    it('should return true for expired token', () => {
      const expiredToken = createMockJwt(-60); // Expired 60s ago
      expect(isTokenExpired(expiredToken)).toBe(true);
    });

    it('should return false for valid future token', () => {
      const validToken = createMockJwt(3600); // Valid for 1h
      expect(isTokenExpired(validToken)).toBe(false);
    });

    it('should return true for null or empty token', () => {
      expect(isTokenExpired(null)).toBe(true);
      expect(isTokenExpired(undefined)).toBe(true);
      expect(isTokenExpired('')).toBe(true);
    });

    it('should return remaining time in ms', () => {
      const token = createMockJwt(10);
      const remaining = getTokenTimeRemainingMs(token);
      expect(remaining).toBeGreaterThan(8000);
      expect(remaining).toBeLessThanOrEqual(10000);
    });

    it('should return 0 ms for expired token', () => {
      const expiredToken = createMockJwt(-10);
      expect(getTokenTimeRemainingMs(expiredToken)).toBe(0);
    });
  });

  describe('AuthSessionService behavior', () => {
    it('should clear stored token upon initialization if already expired', () => {
      const expired = createMockJwt(-300);
      localStorage.setItem(AUTH_TOKEN_KEY, expired);

      TestBed.configureTestingModule({
        providers: [
          AuthSessionService,
          { provide: Router, useValue: routerSpy }
        ]
      });

      const service = TestBed.inject(AuthSessionService);
      expect(service.getToken()).toBeNull();
      expect(service.isAuthenticated()).toBe(false);
      expect(localStorage.getItem(AUTH_TOKEN_KEY)).toBeNull();
    });

    it('should keep token and set isAuthenticated to true if token is valid', () => {
      const valid = createMockJwt(3600);
      localStorage.setItem(AUTH_TOKEN_KEY, valid);

      TestBed.configureTestingModule({
        providers: [
          AuthSessionService,
          { provide: Router, useValue: routerSpy }
        ]
      });

      const service = TestBed.inject(AuthSessionService);
      expect(service.getToken()).toBe(valid);
      expect(service.isAuthenticated()).toBe(true);
    });

    it('should set token and schedule auto logout', () => {
      vi.useFakeTimers();
      try {
        TestBed.configureTestingModule({
          providers: [
            AuthSessionService,
            { provide: Router, useValue: routerSpy }
          ]
        });

        const service = TestBed.inject(AuthSessionService);
        const token = createMockJwt(2); // Expires in 2 seconds

        service.setToken(token);
        expect(service.getToken()).toBe(token);
        expect(service.isAuthenticated()).toBe(true);
        expect(localStorage.getItem(AUTH_TOKEN_KEY)).toBe(token);

        // Fast forward 2.5 seconds
        vi.advanceTimersByTime(2500);

        expect(service.getToken()).toBeNull();
        expect(service.isAuthenticated()).toBe(false);
        expect(localStorage.getItem(AUTH_TOKEN_KEY)).toBeNull();
        expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
      } finally {
        vi.useRealTimers();
      }
    });

    it('should clear token and navigate on logout', () => {
      TestBed.configureTestingModule({
        providers: [
          AuthSessionService,
          { provide: Router, useValue: routerSpy }
        ]
      });

      const service = TestBed.inject(AuthSessionService);
      const token = createMockJwt(3600);
      service.setToken(token);

      service.logout();

      expect(service.getToken()).toBeNull();
      expect(service.isAuthenticated()).toBe(false);
      expect(localStorage.getItem(AUTH_TOKEN_KEY)).toBeNull();
      expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
    });
  });
});
