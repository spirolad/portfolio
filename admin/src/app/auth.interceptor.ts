import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError } from 'rxjs';
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  return next(req).pipe(
    catchError((err) => {
      // If unauthorized, redirect to login
      if (err.status === 401) {
        localStorage.removeItem('portfolio-admin-token');
        router.navigate(['/login']);
      }
      throw err;
    })
  );
};
