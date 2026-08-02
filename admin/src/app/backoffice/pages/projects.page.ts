import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { finalize } from 'rxjs';
import { ProjectService } from '../../api/generated/api/project.service';
import { ProjectResponse } from '../../api/generated/model/projectResponse';
import { ProjectUploadRequest } from '../../api/generated/model/projectUploadRequest';
import { errorMessage, fileToBase64, linesFromText, textFromLines, mimeTypeFromBase64 } from '../../admin-utils';
import { SectionHeaderComponent } from '../shared/section-header.component';

@Component({
  selector: 'app-projects-page',
  standalone: true,
  imports: [ReactiveFormsModule, MatButtonModule, MatCardModule, MatFormFieldModule, MatInputModule, MatProgressBarModule, SectionHeaderComponent],
  templateUrl: './projects.page.html'
})
export class ProjectsPage implements OnInit {
  private readonly service = inject(ProjectService);
  private readonly fb = inject(FormBuilder);

  protected readonly projects = signal<ProjectResponse[]>([]);
  protected readonly projectId = signal<number | null>(null);
  protected readonly loading = signal(false);
  protected readonly saving = signal(false);
  protected readonly error = signal('');

  protected readonly form = this.fb.nonNullable.group({
    name: ['', Validators.required],
    summary: ['', Validators.required],
    description: [''],
    link: [''],
    screenshotsText: [''],
    technologiesText: ['']
  });

  // expose helper to template
  protected readonly linesFromText = linesFromText;

  ngOnInit(): void {
    void this.load();
  }

  protected load(): void {
    this.loading.set(true);
    this.error.set('');

    this.service
      .getProjects()
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (value) => this.projects.set(value),
        error: (error) => this.error.set(errorMessage(error))
      });
  }

  protected editProject(item: ProjectResponse): void {
    this.projectId.set(item.id ?? null);
    this.form.patchValue({
      name: item.name ?? '',
      summary: item.summary ?? '',
      description: item.description ?? '',
      link: item.link ?? '',
      screenshotsText: textFromLines(item.screenshots),
      technologiesText: textFromLines(item.technologies)
    });
  }

  protected reset(): void {
    this.projectId.set(null);
    this.form.reset();
  }

  protected save(): void {
    if (this.form.invalid) {
      return;
    }

    this.saving.set(true);

    const payload: ProjectUploadRequest = {
      name: this.form.value.name ?? '',
      summary: this.form.value.summary ?? '',
      description: this.form.value.description || undefined,
      link: this.form.value.link || undefined,
      screenshots: linesFromText(this.form.value.screenshotsText),
      technologies: linesFromText(this.form.value.technologiesText)
    };

    const request$ = this.projectId() === null
      ? this.service.createProject(payload)
      : this.service.updateProject(this.projectId() ?? 0, payload);

    request$
      .pipe(finalize(() => this.saving.set(false)))
      .subscribe({
        next: () => {
          this.reset();
          this.load();
        },
        error: (error) => this.error.set(errorMessage(error))
      });
  }

  protected deleteCurrent(): void {
    const currentId = this.projectId();
    if (currentId === null || !confirm('Supprimer ce projet ?')) {
      return;
    }

    this.service.deleteProject(currentId).subscribe({
      next: () => {
        this.reset();
        this.load();
      },
      error: (error) => this.error.set(errorMessage(error))
    });
  }

  protected async appendScreenshots(event: Event): Promise<void> {
    const input = event.target as HTMLInputElement | null;
    const files = Array.from(input?.files ?? []);
    if (files.length === 0) {
      return;
    }

    const current = linesFromText(this.form.value.screenshotsText);
    const encodedFiles = await Promise.all(files.map((file) => fileToBase64(file)));
    this.form.patchValue({ screenshotsText: [...current, ...encodedFiles].join('\n') });
  }

  protected getImageSrc(value: string | null | undefined): string | null {
    if (!value) return null;
    const base = value.includes(',') ? value.split(',')[1] ?? '' : value;
    const mime = mimeTypeFromBase64(base);
    return `data:${mime};base64,${base}`;
  }

  protected projectSummary(item: ProjectResponse): string {
    return item.summary || item.description || 'Aucun résumé';
  }

  protected removeScreenshot(screenshot: string): void {
    const current = linesFromText(this.form.value.screenshotsText);
    const updated = current.filter((s) => s !== screenshot);
    this.form.patchValue({ screenshotsText: updated.join('\n') });
  }
}
