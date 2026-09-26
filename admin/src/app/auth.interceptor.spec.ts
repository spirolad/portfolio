import { TestBed } from '@angular/core/testing';
import { HttpClient, HttpErrorResponse, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { AuthSessionService } from './auth-session.service';
import { authInterceptor } from './auth.interceptor';

describe('authInterceptor', () => {
  let http: HttpClient;
  let httpMock: HttpTestingController;
  let authSessionMock: {
    logout: ReturnType<typeof vi.fn>;
    isExpired: ReturnType<typeof vi.fn>;
    getToken: ReturnType<typeof vi.fn>;
  };

  beforeEach(() => {
    authSessionMock = {
      logout: vi.fn(),
      isExpired: vi.fn().mockReturnValue(false),
      getToken: vi.fn().mockReturnValue('valid-token')
    };

    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting(),
        { provide: AuthSessionService, useValue: authSessionMock }
      ]
    });

    http = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should call logout and redirect to login when response status is 401 on protected requests', () => {
    http.get('/api/profile').subscribe({
      next: () => expect.fail('Should have failed with 401'),
      error: (err: HttpErrorResponse) => {
        expect(err.status).toBe(401);
      }
    });

    const req = httpMock.expectOne('/api/profile');
    req.flush({ error: 'Token expired' }, { status: 401, statusText: 'Unauthorized' });

    expect(authSessionMock.logout).toHaveBeenCalled();
  });

  it('should not call logout when status is 401 on /auth/login request', () => {
    http.post('/api/auth/login', { username: 'admin', password: 'wrong' }).subscribe({
      next: () => expect.fail('Should have failed with 401'),
      error: (err: HttpErrorResponse) => {
        expect(err.status).toBe(401);
      }
    });

    const req = httpMock.expectOne('/api/auth/login');
    req.flush({ error: 'Invalid credentials' }, { status: 401, statusText: 'Unauthorized' });

    expect(authSessionMock.logout).not.toHaveBeenCalled();
  });

  it('should call logout proactively if token is already expired before protected request', () => {
    authSessionMock.isExpired.mockReturnValue(true);
    authSessionMock.getToken.mockReturnValue('expired-token');

    http.get('/api/skills').subscribe({
      next: () => {}
    });

    const req = httpMock.expectOne('/api/skills');
    req.flush([]);

    expect(authSessionMock.logout).toHaveBeenCalled();
  });
});
