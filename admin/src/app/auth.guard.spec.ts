import { TestBed } from '@angular/core/testing';
import { Router, UrlTree } from '@angular/router';
import { AuthSessionService } from './auth-session.service';
import { authGuard } from './auth.guard';

describe('authGuard', () => {
  let authSessionMock: {
    isAuthenticated: ReturnType<typeof vi.fn>;
    clearToken: ReturnType<typeof vi.fn>;
  };
  let routerMock: {
    createUrlTree: ReturnType<typeof vi.fn>;
  };

  beforeEach(() => {
    authSessionMock = {
      isAuthenticated: vi.fn(),
      clearToken: vi.fn()
    };
    routerMock = {
      createUrlTree: vi.fn().mockImplementation((commands) => ({ path: commands } as unknown as UrlTree))
    };

    TestBed.configureTestingModule({
      providers: [
        { provide: AuthSessionService, useValue: authSessionMock },
        { provide: Router, useValue: routerMock }
      ]
    });
  });

  it('should allow access when authenticated', () => {
    authSessionMock.isAuthenticated.mockReturnValue(true);

    const result = TestBed.runInInjectionContext(() => authGuard({} as any, {} as any));
    expect(result).toBe(true);
    expect(authSessionMock.clearToken).not.toHaveBeenCalled();
    expect(routerMock.createUrlTree).not.toHaveBeenCalled();
  });

  it('should redirect to /login and clear token when token is expired or absent', () => {
    authSessionMock.isAuthenticated.mockReturnValue(false);

    const result = TestBed.runInInjectionContext(() => authGuard({} as any, {} as any));
    expect(authSessionMock.clearToken).toHaveBeenCalled();
    expect(routerMock.createUrlTree).toHaveBeenCalledWith(['/login']);
    expect(result).toEqual({ path: ['/login'] });
  });
});
