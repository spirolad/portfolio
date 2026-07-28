import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
export const authGuard: CanActivateFn = () => {
  const router = inject(Router);
  const token = localStorage.getItem('portfolio-admin-token');
  // Simple token presence check. An HTTP interceptor can log out if 401.
  if (token) {
    return true;
  }
  return router.createUrlTree(['/login']);
};
