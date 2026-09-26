import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';

import { Configuration } from './api/generated/configuration';
import { BASE_PATH } from './api/generated/variables';
import { routes } from './app.routes';
import { authInterceptor } from './auth.interceptor';
import { AuthSessionService } from './auth-session.service';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideHttpClient(withInterceptors([authInterceptor])),
    provideAnimations(),
    provideRouter(routes),
    { provide: BASE_PATH, useValue: 'http://localhost:8080/api' },
    {
      provide: Configuration,
      deps: [AuthSessionService],
      useFactory: (authSession: AuthSessionService) =>
        new Configuration({
          basePath: 'http://localhost:8080/api',
          accessToken: () => authSession.getToken() ?? ''
        })
    }
  ]
};
