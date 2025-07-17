import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITypeUtilisateur } from 'app/entities/type-utilisateur/type-utilisateur.model';
import { TypeUtilisateurService } from 'app/entities/type-utilisateur/service/type-utilisateur.service';
import { IRole } from 'app/entities/role/role.model';
import { RoleService } from 'app/entities/role/service/role.service';
import { UtilisateurService } from '../service/utilisateur.service';
import { IUtilisateur, NewUtilisateur } from '../utilisateur.model';
import { UtilisateurFormGroup, UtilisateurFormService } from './utilisateur-form.service';

@Component({
  selector: 'jhi-utilisateur-update',
  templateUrl: './utilisateur-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UtilisateurUpdateComponent implements OnInit {
  isSaving = false;
  utilisateur: IUtilisateur | null = null;

  typeUtilisateursSharedCollection: ITypeUtilisateur[] = [];
  rolesSharedCollection: IRole[] = [];

  protected utilisateurService = inject(UtilisateurService);
  protected utilisateurFormService = inject(UtilisateurFormService);
  protected typeUtilisateurService = inject(TypeUtilisateurService);
  protected roleService = inject(RoleService);
  protected activatedRoute = inject(ActivatedRoute);

  editForm: UtilisateurFormGroup = this.utilisateurFormService.createUtilisateurFormGroup();

  compareTypeUtilisateur = (o1: ITypeUtilisateur | null, o2: ITypeUtilisateur | null): boolean =>
    this.typeUtilisateurService.compareTypeUtilisateur(o1, o2);

  compareRole = (o1: IRole | null, o2: IRole | null): boolean => this.roleService.compareRole(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ utilisateur }) => {
      this.utilisateur = utilisateur;
      if (utilisateur) {
        this.updateForm(utilisateur);
      }
      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const utilisateur = this.utilisateurFormService.getUtilisateur(this.editForm);

    if (utilisateur.id !== null) {
      this.subscribeToSaveResponse(this.utilisateurService.update(utilisateur));
    } else {
      const { id, ...newUtilisateur } = utilisateur;
      this.subscribeToSaveResponse(this.utilisateurService.create({ ...newUtilisateur, id: null } as NewUtilisateur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUtilisateur>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(utilisateur: IUtilisateur): void {
    this.utilisateur = utilisateur;
    this.utilisateurFormService.resetForm(this.editForm, utilisateur);

    this.typeUtilisateursSharedCollection = this.typeUtilisateurService.addTypeUtilisateurToCollectionIfMissing<ITypeUtilisateur>(
      this.typeUtilisateursSharedCollection,
      utilisateur.typeUtilisateur,
    );
    this.rolesSharedCollection = this.roleService.addRoleToCollectionIfMissing<IRole>(
      this.rolesSharedCollection,
      ...(utilisateur.roles ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.typeUtilisateurService
      .query()
      .pipe(map((res: HttpResponse<ITypeUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((typeUtilisateurs: ITypeUtilisateur[]) =>
          this.typeUtilisateurService.addTypeUtilisateurToCollectionIfMissing<ITypeUtilisateur>(
            typeUtilisateurs,
            this.utilisateur?.typeUtilisateur,
          ),
        ),
      )
      .subscribe((typeUtilisateurs: ITypeUtilisateur[]) => (this.typeUtilisateursSharedCollection = typeUtilisateurs));

    this.roleService
      .query()
      .pipe(map((res: HttpResponse<IRole[]>) => res.body ?? []))
      .pipe(map((roles: IRole[]) => this.roleService.addRoleToCollectionIfMissing<IRole>(roles, ...(this.utilisateur?.roles ?? []))))
      .subscribe((roles: IRole[]) => (this.rolesSharedCollection = roles));
  }
}
