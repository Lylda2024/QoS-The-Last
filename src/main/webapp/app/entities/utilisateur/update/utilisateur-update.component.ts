import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { finalize, map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { CommonModule } from '@angular/common';

import { IUtilisateur, NewUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';

import { IRole } from 'app/entities/role/role.model';
import { RoleService } from 'app/entities/role/service/role.service';

import { ITypeUtilisateur } from 'app/entities/type-utilisateur/type-utilisateur.model';
import { TypeUtilisateurService } from 'app/entities/type-utilisateur/service/type-utilisateur.service';

import SharedModule from 'app/shared/shared.module'; // ✅ important pour jhi-alert-error, fa-icon

@Component({
  standalone: true,
  selector: 'jhi-utilisateur-update',
  templateUrl: './utilisateur-update.component.html',
  imports: [
    CommonModule, // ✅ Permet d’utiliser *ngIf, *ngFor, etc.
    ReactiveFormsModule, // ✅ Permet [formGroup], formControlName
    SharedModule, // ✅ Pour fa-icon, jhi-alert-error
  ],
})
export class UtilisateurUpdateComponent implements OnInit {
  isSaving = false;

  /** Collections pour les <select> */
  rolesSharedCollection: IRole[] = [];
  typeUtilisateursSharedCollection: ITypeUtilisateur[] = [];

  /** ✅ Formulaire typé */
  editForm = this.fb.group({
    id: this.fb.control<number | null>(null),
    nom: this.fb.control<string | null>(null, { validators: [Validators.required] }),
    prenom: this.fb.control<string | null>(null, { validators: [Validators.required] }),
    email: this.fb.control<string | null>(null, { validators: [Validators.required, Validators.email] }),
    motDePasse: this.fb.control<string | null>(null, { validators: [Validators.required] }),
    typeUtilisateur: this.fb.control<ITypeUtilisateur | null>(null, { validators: [Validators.required] }),
    roles: this.fb.control<IRole[]>([], { validators: [Validators.required] }),
  });

  constructor(
    protected utilisateurService: UtilisateurService,
    protected roleService: RoleService,
    protected typeUtilisateurService: TypeUtilisateurService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
  ) {}

  /** ✅ Initialisation */
  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ utilisateur }) => {
      if (utilisateur) {
        this.updateForm(utilisateur);
      }
      this.loadRelationshipsOptions();
    });
  }

  /** ✅ Retour arrière */
  previousState(): void {
    window.history.back();
  }

  /** ✅ Sauvegarde */
  save(): void {
    this.isSaving = true;
    const utilisateur = this.createFromForm();

    let saveObservable: Observable<HttpResponse<IUtilisateur>>;
    if (utilisateur.id !== null) {
      // Mise à jour
      saveObservable = this.utilisateurService.update(utilisateur);
    } else {
      // Création
      saveObservable = this.utilisateurService.create(utilisateur as NewUtilisateur);
    }

    saveObservable.pipe(finalize(() => (this.isSaving = false))).subscribe({
      next: () => this.previousState(),
      error: error => {
        console.error('❌ Erreur lors de la sauvegarde', error);
        alert('Erreur lors de la sauvegarde. Veuillez vérifier les données et réessayer.');
      },
    });
  }

  /** ✅ Remplissage du formulaire lors de la modification */
  protected updateForm(utilisateur: IUtilisateur): void {
    this.editForm.patchValue({
      id: utilisateur.id,
      nom: utilisateur.nom,
      prenom: utilisateur.prenom,
      email: utilisateur.email,
      motDePasse: utilisateur.motDePasse,
      typeUtilisateur: utilisateur.typeUtilisateur,
      roles: utilisateur.roles ?? [],
    });

    // Ajoute les rôles déjà liés
    this.rolesSharedCollection = this.addRoleToCollectionIfMissing(this.rolesSharedCollection, ...(utilisateur.roles ?? []));

    // Ajoute le typeUtilisateur déjà lié
    if (utilisateur.typeUtilisateur) {
      this.typeUtilisateursSharedCollection = this.addTypeUtilisateurToCollectionIfMissing(
        this.typeUtilisateursSharedCollection,
        utilisateur.typeUtilisateur,
      );
    }
  }

  /** ✅ Chargement des listes (relations) */
  protected loadRelationshipsOptions(): void {
    this.roleService
      .query()
      .pipe(map((res: HttpResponse<IRole[]>) => res.body ?? []))
      .subscribe(roles => {
        this.rolesSharedCollection = this.addRoleToCollectionIfMissing(roles, ...(this.editForm.get('roles')!.value ?? []));
      });

    this.typeUtilisateurService
      .query()
      .pipe(map((res: HttpResponse<ITypeUtilisateur[]>) => res.body ?? []))
      .subscribe(types => {
        this.typeUtilisateursSharedCollection = this.addTypeUtilisateurToCollectionIfMissing(
          types,
          this.editForm.get('typeUtilisateur')!.value,
        );
      });
  }

  /** ✅ Construction d’un objet IUtilisateur depuis le formulaire */
  protected createFromForm(): IUtilisateur | NewUtilisateur {
    const rawValue = this.editForm.getRawValue();
    return {
      id: rawValue.id ?? null,
      nom: rawValue.nom ?? '',
      prenom: rawValue.prenom ?? '',
      email: rawValue.email ?? '',
      motDePasse: rawValue.motDePasse ?? '',
      typeUtilisateur: rawValue.typeUtilisateur!,
      roles: rawValue.roles ?? [],
    };
  }

  /** ✅ compareWith pour Angular */
  compareRole = (o1: IRole | null, o2: IRole | null): boolean => (o1 && o2 ? o1.id === o2.id : o1 === o2);

  compareTypeUtilisateur = (o1: ITypeUtilisateur | null, o2: ITypeUtilisateur | null): boolean => (o1 && o2 ? o1.id === o2.id : o1 === o2);

  /** ✅ Helpers pour éviter les doublons dans les listes */
  protected addRoleToCollectionIfMissing(roleCollection: IRole[], ...rolesToCheck: (IRole | null | undefined)[]): IRole[] {
    const roles: IRole[] = rolesToCheck.filter((r): r is IRole => r != null);
    for (const role of roles) {
      if (!roleCollection.find(r => r.id === role.id)) {
        roleCollection.push(role);
      }
    }
    return roleCollection;
  }

  protected addTypeUtilisateurToCollectionIfMissing(
    typeCollection: ITypeUtilisateur[],
    ...typesToCheck: (ITypeUtilisateur | null | undefined)[]
  ): ITypeUtilisateur[] {
    const types: ITypeUtilisateur[] = typesToCheck.filter((t): t is ITypeUtilisateur => t != null);
    for (const type of types) {
      if (!typeCollection.find(t => t.id === type.id)) {
        typeCollection.push(type);
      }
    }
    return typeCollection;
  }
}
