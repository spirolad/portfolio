import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSelectModule } from '@angular/material/select';
import { finalize } from 'rxjs';
import { SkillService } from '../../api/generated/api/skill.service';
import { CategoryRequest } from '../../api/generated/model/categoryRequest';
import { CategoryResponse } from '../../api/generated/model/categoryResponse';
import { SkillRequest } from '../../api/generated/model/skillRequest';
import { SkillResponse } from '../../api/generated/model/skillResponse';
import { errorMessage } from '../../admin-utils';
import { SectionHeaderComponent } from '../shared/section-header.component';

@Component({
  selector: 'app-skills-page',
  standalone: true,
  imports: [ReactiveFormsModule, MatButtonModule, MatCardModule, MatFormFieldModule, MatInputModule, MatProgressBarModule, MatSelectModule, SectionHeaderComponent],
  templateUrl: './skills.page.html'
})
export class SkillsPage implements OnInit {
  private readonly service = inject(SkillService);
  private readonly fb = inject(FormBuilder);

  protected readonly categories = signal<CategoryResponse[]>([]);
  protected readonly skills = signal<SkillResponse[]>([]);
  protected readonly categoryId = signal<number | null>(null);
  protected readonly skillId = signal<number | null>(null);
  protected readonly loadingCategories = signal(false);
  protected readonly loadingSkills = signal(false);
  protected readonly savingCategory = signal(false);
  protected readonly savingSkill = signal(false);
  protected readonly categoryError = signal('');
  protected readonly skillError = signal('');

  protected readonly categoryForm = this.fb.nonNullable.group({
    name: ['', Validators.required]
  });

  protected readonly skillForm = this.fb.nonNullable.group({
    name: ['', Validators.required],
    category: [null as number | null, Validators.required]
  });

  ngOnInit(): void {
    void this.loadCategories();
    void this.loadSkills();
  }

  protected loadCategories(): void {
    this.loadingCategories.set(true);
    this.categoryError.set('');

    this.service
      .getSkillCategories()
      .pipe(finalize(() => this.loadingCategories.set(false)))
      .subscribe({
        next: (value) => {
          this.categories.set(value);
          if (this.skillForm.controls.category.value === null && value.length > 0 && value[0].id !== undefined) {
            this.skillForm.patchValue({ category: value[0].id });
          }
        },
        error: (error) => this.categoryError.set(errorMessage(error))
      });
  }

  protected loadSkills(): void {
    this.loadingSkills.set(true);
    this.skillError.set('');

    this.service
      .getSkills()
      .pipe(finalize(() => this.loadingSkills.set(false)))
      .subscribe({
        next: (value) => this.skills.set(value),
        error: (error) => this.skillError.set(errorMessage(error))
      });
  }

  protected editCategory(item: CategoryResponse): void {
    this.categoryId.set(item.id ?? null);
    this.categoryForm.patchValue({ name: item.name });
  }

  protected resetCategory(): void {
    this.categoryId.set(null);
    this.categoryForm.reset();
  }

  protected saveCategory(): void {
    if (this.categoryForm.invalid) {
      return;
    }

    this.savingCategory.set(true);
    const payload: CategoryRequest = { name: this.categoryForm.value.name ?? '' };
    const request$ = this.categoryId() === null
      ? this.service.createSkillCategory(payload)
      : this.service.updateSkillCategory(this.categoryId() ?? 0, payload);

    request$
      .pipe(finalize(() => this.savingCategory.set(false)))
      .subscribe({
        next: () => {
          this.resetCategory();
          this.loadCategories();
        },
        error: (error) => this.categoryError.set(errorMessage(error))
      });
  }

  protected deleteCurrentCategory(): void {
    const currentId = this.categoryId();
    if (currentId === null || !confirm('Supprimer cette categorie ?')) {
      return;
    }

    this.service.deleteSkillCategory(currentId).subscribe({
      next: () => {
        this.resetCategory();
        this.loadCategories();
      },
      error: (error) => this.categoryError.set(errorMessage(error))
    });
  }

  protected editSkill(item: SkillResponse): void {
    this.skillId.set(item.id ?? null);
    this.skillForm.patchValue({
      name: item.name,
      category: item.category?.id ?? null
    });
  }

  protected resetSkill(): void {
    this.skillId.set(null);
    this.skillForm.reset();
    if (this.categories().length > 0) {
      const firstCategory = this.categories()[0];
      if (firstCategory.id !== undefined) {
        this.skillForm.patchValue({ category: firstCategory.id });
      }
    }
  }

  protected saveSkill(): void {
    const categoryId = this.skillForm.controls.category.value;
    if (this.skillForm.invalid || categoryId === null) {
      return;
    }

    this.savingSkill.set(true);

    const payload: SkillRequest = {
      name: this.skillForm.value.name ?? '',
      categoryId
    };

    const request$ = this.skillId() === null
      ? this.service.createSkill(payload)
      : this.service.updateSkill(this.skillId() ?? 0, payload);

    request$
      .pipe(finalize(() => this.savingSkill.set(false)))
      .subscribe({
        next: () => {
          this.resetSkill();
          this.loadSkills();
        },
        error: (error) => this.skillError.set(errorMessage(error))
      });
  }

  protected deleteCurrentSkill(): void {
    const currentId = this.skillId();
    if (currentId === null || !confirm('Supprimer ce skill ?')) {
      return;
    }

    this.service.deleteSkill(currentId).subscribe({
      next: () => {
        this.resetSkill();
        this.loadSkills();
      },
      error: (error) => this.skillError.set(errorMessage(error))
    });
  }
}
