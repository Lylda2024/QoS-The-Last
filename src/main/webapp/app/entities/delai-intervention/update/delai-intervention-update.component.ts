import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import dayjs from 'dayjs';

import { DelaiInterventionFormService } from './delai-intervention-form.service';
import { DelaiInterventionService } from '../../delai-intervention/service/delai-intervention.service';
import { DegradationService } from 'app/entities/degradation/service/degradation.service';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { IDegradation } from 'app/entities/degradation/degradation.model';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { IDelaiIntervention, NewDelaiIntervention, StatutDelaiIntervention } from '../delai-intervention.model';

@Component({
  selector: 'jhi-delai-intervention-update',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './delai-intervention-update.component.html',
})
export class DelaiInterventionUpdateComponent implements OnInit {
  editForm = this.formService.createDelaiInterventionFormGroup();
  isSaving = false;
  compareUtilisateurs(u1: any, u2: any): boolean {
    return u1 && u2 ? u1.id === u2.id : u1 === u2;
  }

  degradations: IDegradation[] = [];
  utilisateurs: IUtilisateur[] = [];
  statutOptions = Object.values(StatutDelaiIntervention);

  constructor(
    private formService: DelaiInterventionFormService,
    private service: DelaiInterventionService,
    private degradationService: DegradationService,
    private utilisateurService: UtilisateurService,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const idDegradation = params['idDegradation'];

      if (idDegradation) {
        // Cas création avec une dégradation fournie
        this.degradationService.find(idDegradation).subscribe(res => {
          const degradation = res.body;

          this.editForm = this.formService.createDelaiInterventionFormGroup({
            id: null, // création : id masqué
            dateDebut: dayjs(), // date par défaut
          });

          // Empêche modification manuelle
          this.editForm.get('degradation')?.disable();
        });
      } else {
        // cas édition ou création normale
        this.route.data.subscribe(({ delaiIntervention }) => {
          this.editForm = this.formService.createDelaiInterventionFormGroup(delaiIntervention);
        });
      }

      this.loadUtilisateurs();
    });
  }

  loadDegradations(): void {
    this.degradationService.query().subscribe(res => (this.degradations = res.body ?? []));
  }

  loadUtilisateurs(): void {
    this.utilisateurService.queryActeurs().subscribe(res => {
      this.utilisateurs = res.body ?? [];
    });
  }

  save(): void {
    this.editForm.markAllAsTouched(); // ✅ important
    if (this.editForm.invalid) {
      return;
    }

    this.isSaving = true;
    const entity = this.createFromForm();

    if (entity.id === null) {
      this.service.create(entity as NewDelaiIntervention).subscribe({
        next: () => this.onSaveSuccess(),
        error: () => this.onSaveError(),
      });
    } else {
      this.service.update(entity as IDelaiIntervention).subscribe({
        next: () => this.onSaveSuccess(),
        error: () => this.onSaveError(),
      });
    }
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  previousState(): void {
    window.history.back();
  }

  protected createFromForm(): IDelaiIntervention | NewDelaiIntervention {
    const rawValue = this.editForm.getRawValue();

    const statut: StatutDelaiIntervention | undefined = rawValue.statut ?? undefined;

    if (rawValue.id === null || rawValue.id === undefined) {
      return {
        id: null, // corrigé ici
        dateDebut: rawValue.dateDebut ?? undefined,
        dateLimite: rawValue.dateLimite ?? undefined,
        commentaire: rawValue.commentaire ?? undefined,
        statut: statut,
        acteur: rawValue.acteur ?? null,
      };
    } else {
      return {
        id: rawValue.id,
        dateDebut: rawValue.dateDebut ?? undefined,
        dateLimite: rawValue.dateLimite ?? undefined,
        commentaire: rawValue.commentaire ?? undefined,
        statut: statut,
        acteur: rawValue.acteur ?? null,
      };
    }
  }
}
