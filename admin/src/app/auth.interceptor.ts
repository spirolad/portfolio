import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError } from 'rxjs';
import { AuthSessionService } from './auth-session.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authSession = inject(AuthSessionService);
  const isAuthRequest = req.url.includes('/auth/login');

  // If the stored token is already expired before making a protected request, log out immediately
  if (!isAuthRequest && authSession.isExpired() && authSession.getToken()) {
    authSession.logout();
  }

  return next(req).pipe(
    catchError((err) => {
      // If unauthorized (and not an invalid credentials error on /auth/login), redirect to login
      if (err.status === 401 && !isAuthRequest) {
        authSession.logout();
      }
      throw err;
    })
  );
};
