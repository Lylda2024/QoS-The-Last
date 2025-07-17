import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITypeUtilisateur } from '../type-utilisateur.model';
import { TypeUtilisateurService } from '../service/type-utilisateur.service';
import { TypeUtilisateurFormGroup, TypeUtilisateurFormService } from './type-utilisateur-form.service';

@Component({
  selector: 'jhi-type-utilisateur-update',
  templateUrl: './type-utilisateur-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TypeUtilisateurUpdateComponent implements OnInit {
  isSaving = false;
  typeUtilisateur: ITypeUtilisateur | null = null;

  protected typeUtilisateurService = inject(TypeUtilisateurService);
  protected typeUtilisateurFormService = inject(TypeUtilisateurFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TypeUtilisateurFormGroup = this.typeUtilisateurFormService.createTypeUtilisateurFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeUtilisateur }) => {
      this.typeUtilisateur = typeUtilisateur;
      if (typeUtilisateur) {
        this.updateForm(typeUtilisateur);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeUtilisateur = this.typeUtilisateurFormService.getTypeUtilisateur(this.editForm);

    if (typeUtilisateur.id !== null) {
      this.subscribeToSaveResponse(this.typeUtilisateurService.update(typeUtilisateur));
    } else {
      const { id, ...newEntity } = typeUtilisateur;
      this.subscribeToSaveResponse(this.typeUtilisateurService.create({ ...newEntity, id: null }));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeUtilisateur>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(typeUtilisateur: ITypeUtilisateur): void {
    this.typeUtilisateur = typeUtilisateur;
    this.typeUtilisateurFormService.resetForm(this.editForm, typeUtilisateur);
  }
}
