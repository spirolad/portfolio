import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { finalize } from 'rxjs';
import { PortfolioService } from '../../api/generated/api/portfolio.service';
import { PortfolioRequest } from '../../api/generated/model/portfolioRequest';
import { PortfolioResponse } from '../../api/generated/model/portfolioResponse';
import { errorMessage, fileToBase64, mimeTypeFromBase64 } from '../../admin-utils';
import { SectionHeaderComponent } from '../shared/section-header.component';

@Component({
  selector: 'app-profile-page',
  standalone: true,
  imports: [ReactiveFormsModule, MatButtonModule, MatCardModule, MatFormFieldModule, MatInputModule, MatProgressBarModule, SectionHeaderComponent],
  templateUrl: './profile.page.html'
})
export class ProfilePage implements OnInit {
  private readonly service = inject(PortfolioService);
  private readonly fb = inject(FormBuilder);

  protected readonly loading = signal(false);
  protected readonly saving = signal(false);
  protected readonly error = signal('');
  protected readonly portfolio = signal<PortfolioResponse | null>(null);

  protected readonly form = this.fb.nonNullable.group({
    name: ['', Validators.required],
    email: ['', [Validators.email]],
    currentPosition: [''],
    photo: ['']
  });

  ngOnInit(): void {
    void this.load();
  }

  protected load(): void {
    this.loading.set(true);
    this.error.set('');

    this.service
      .getPortfolio()
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (value) => {
          this.portfolio.set(value);
          const generalInfo = value.generalInfo;
          this.form.patchValue({
            name: generalInfo?.name ?? '',
            email: generalInfo?.email ?? '',
            currentPosition: generalInfo?.currentPosition ?? '',
            photo: generalInfo?.photo ?? ''
          });
        },
        error: (error) => {
          console.error(error)
          this.error.set(errorMessage(error))
        }
      });
  }

  protected save(): void {
    if (this.form.invalid) {
      return;
    }

    this.saving.set(true);
    this.error.set('');

    const payload: PortfolioRequest = {
      name: this.form.value.name ?? undefined,
      email: this.form.value.email ?? undefined,
      currentPosition: this.form.value.currentPosition ?? undefined,
      photo: this.form.value.photo ?? undefined
    };

    this.service
      .updatePortfolio(payload)
      .pipe(finalize(() => this.saving.set(false)))
      .subscribe({
        next: (value) => this.portfolio.set(value),
        error: (error) => this.error.set(errorMessage(error))
      });
  }

  protected async onPhotoSelected(event: Event): Promise<void> {
    const input = event.target as HTMLInputElement | null;
    const file = input?.files?.item(0);
    if (!file) {
      return;
    }

    this.form.patchValue({ photo: await fileToBase64(file) });
  }

  protected getImageSrc(value: string | null | undefined): string | null {
    if (!value) return null;
    const base = value.includes(',') ? value.split(',')[1] ?? '' : value;
    const mime = mimeTypeFromBase64(base);
    return `data:${mime};base64,${base}`;
  }

  protected clearPhoto(): void {
    this.form.patchValue({ photo: '' });
  }
}
