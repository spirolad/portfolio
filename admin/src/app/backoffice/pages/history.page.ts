import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { finalize } from 'rxjs';
import { EducationService } from '../../api/generated/api/education.service';
import { ExperienceService } from '../../api/generated/api/experience.service';
import { EducationRequest } from '../../api/generated/model/educationRequest';
import { EducationResponse } from '../../api/generated/model/educationResponse';
import { ExperienceRequest } from '../../api/generated/model/experienceRequest';
import { ExperienceResponse } from '../../api/generated/model/experienceResponse';
import { errorMessage, linesFromText, textFromLines } from '../../admin-utils';
import { SectionHeaderComponent } from '../shared/section-header.component';

@Component({
  selector: 'app-history-page',
  standalone: true,
  imports: [ReactiveFormsModule, MatButtonModule, MatCardModule, MatFormFieldModule, MatInputModule, MatProgressBarModule, SectionHeaderComponent],
  templateUrl: './history.page.html'
})
export class HistoryPage implements OnInit {
  private readonly educationService = inject(EducationService);
  private readonly experienceService = inject(ExperienceService);
  private readonly fb = inject(FormBuilder);

  protected readonly educations = signal<EducationResponse[]>([]);
  protected readonly experiences = signal<ExperienceResponse[]>([]);
  protected readonly educationId = signal<number | null>(null);
  protected readonly experienceId = signal<number | null>(null);
  protected readonly loadingEducations = signal(false);
  protected readonly loadingExperiences = signal(false);
  protected readonly savingEducation = signal(false);
  protected readonly savingExperience = signal(false);
  protected readonly educationError = signal('');
  protected readonly experienceError = signal('');

  protected readonly educationForm = this.fb.nonNullable.group({
    institution: ['', Validators.required],
    degree: ['', Validators.required],
    startDate: ['', Validators.required],
    endDate: ['']
  });

  protected readonly experienceForm = this.fb.nonNullable.group({
    company: ['', Validators.required],
    position: ['', Validators.required],
    missionText: [''],
    startDate: ['', Validators.required],
    endDate: ['']
  });

  ngOnInit(): void {
    void this.loadEducations();
    void this.loadExperiences();
  }

  protected loadEducations(): void {
    this.loadingEducations.set(true);
    this.educationError.set('');

    this.educationService
      .getEducations()
      .pipe(finalize(() => this.loadingEducations.set(false)))
      .subscribe({
        next: (value) => this.educations.set(value),
        error: (error) => this.educationError.set(errorMessage(error))
      });
  }

  protected loadExperiences(): void {
    this.loadingExperiences.set(true);
    this.experienceError.set('');

    this.experienceService
      .getExperiences()
      .pipe(finalize(() => this.loadingExperiences.set(false)))
      .subscribe({
        next: (value) => this.experiences.set(value),
        error: (error) => this.experienceError.set(errorMessage(error))
      });
  }

  protected editEducation(item: EducationResponse): void {
    this.educationId.set(item.id ?? null);
    this.educationForm.patchValue({
      institution: item.institution,
      degree: item.degree,
      startDate: item.startDate,
      endDate: item.endDate ?? ''
    });
  }

  protected resetEducation(): void {
    this.educationId.set(null);
    this.educationForm.reset();
  }

  protected saveEducation(): void {
    if (this.educationForm.invalid) {
      return;
    }

    this.savingEducation.set(true);

    const payload: EducationRequest = {
      institution: this.educationForm.value.institution ?? '',
      degree: this.educationForm.value.degree ?? '',
      startDate: this.educationForm.value.startDate ?? '',
      endDate: this.educationForm.value.endDate || undefined
    };

    const request$ = this.educationId() === null
      ? this.educationService.createEducation(payload)
      : this.educationService.updateEducation(this.educationId() ?? 0, payload);

    request$
      .pipe(finalize(() => this.savingEducation.set(false)))
      .subscribe({
        next: () => {
          this.resetEducation();
          this.loadEducations();
        },
        error: (error) => this.educationError.set(errorMessage(error))
      });
  }

  protected deleteCurrentEducation(): void {
    const currentId = this.educationId();
    if (currentId === null || !confirm('Supprimer cette education ?')) {
      return;
    }

    this.educationService.deleteEducation(currentId).subscribe({
      next: () => {
        this.resetEducation();
        this.loadEducations();
      },
      error: (error) => this.educationError.set(errorMessage(error))
    });
  }

  protected editExperience(item: ExperienceResponse): void {
    this.experienceId.set(item.id ?? null);
    this.experienceForm.patchValue({
      company: item.company,
      position: item.position,
      missionText: textFromLines(item.mission),
      startDate: item.startDate,
      endDate: item.endDate ?? ''
    });
  }

  protected resetExperience(): void {
    this.experienceId.set(null);
    this.experienceForm.reset();
  }

  protected saveExperience(): void {
    if (this.experienceForm.invalid) {
      return;
    }

    this.savingExperience.set(true);

    const payload: ExperienceRequest = {
      company: this.experienceForm.value.company ?? '',
      position: this.experienceForm.value.position ?? '',
      mission: linesFromText(this.experienceForm.value.missionText),
      startDate: this.experienceForm.value.startDate ?? '',
      endDate: this.experienceForm.value.endDate || undefined
    };

    const request$ = this.experienceId() === null
      ? this.experienceService.createExperience(payload)
      : this.experienceService.updateExperience(this.experienceId() ?? 0, payload);

    request$
      .pipe(finalize(() => this.savingExperience.set(false)))
      .subscribe({
        next: () => {
          this.resetExperience();
          this.loadExperiences();
        },
        error: (error) => this.experienceError.set(errorMessage(error))
      });
  }

  protected deleteCurrentExperience(): void {
    const currentId = this.experienceId();
    if (currentId === null || !confirm('Supprimer cette experience ?')) {
      return;
    }

    this.experienceService.deleteExperience(currentId).subscribe({
      next: () => {
        this.resetExperience();
        this.loadExperiences();
      },
      error: (error) => this.experienceError.set(errorMessage(error))
    });
  }
}
