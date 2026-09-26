import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { finalize } from 'rxjs';
import { AuthService } from '../../../api/generated/api/auth.service';
import { LoginRequest } from '../../../api/generated/model/loginRequest';
import { errorMessage } from '../../../admin-utils';
import { AuthSessionService } from '../../../auth-session.service';
@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [ReactiveFormsModule, MatButtonModule, MatCardModule, MatFormFieldModule, MatInputModule, MatProgressBarModule],
  templateUrl: './login.page.html'
})
export class LoginPage {
  private readonly authService = inject(AuthService);
  private readonly authSession = inject(AuthSessionService);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);
  protected readonly loading = signal(false);
  protected readonly error = signal('');
  protected readonly form = this.fb.nonNullable.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });
  protected login(): void {
    if (this.form.invalid) return;
    this.loading.set(true);
    this.error.set('');
    const payload: LoginRequest = {
      username: this.form.value.username as string,
      password: this.form.value.password as string
    };
    this.authService
      .login(payload)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (res) => {
          if (res.token) {
            this.authSession.setToken(res.token);
            this.router.navigate(['/']);
          }
        },
        error: (err) => {
          this.error.set(errorMessage(err));
        }
      });
  }
}
