// delai-intervention-update.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
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

  utilisateurs: IUtilisateur[] = [];
  statutOptions = Object.values(StatutDelaiIntervention);

  compareUtilisateurs = (u1: any, u2: any): boolean => (u1 && u2 ? u1.id === u2.id : u1 === u2);

  constructor(
    private formService: DelaiInterventionFormService,
    private service: DelaiInterventionService,
    private degradationService: DegradationService,
    private utilisateurService: UtilisateurService,
    private route: ActivatedRoute,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const degradationId = params['degradationId'];
      if (degradationId) {
        this.degradationService.find(degradationId).subscribe(res => {
          this.editForm = this.formService.createDelaiInterventionFormGroup({
            id: null,
            dateDebut: dayjs(),
            degradation: res.body,
          });
          this.editForm.get('degradation')?.disable();
        });
      } else {
        this.route.data.subscribe(({ delaiIntervention }) => {
          this.editForm = this.formService.createDelaiInterventionFormGroup(delaiIntervention);
        });
      }
      this.loadUtilisateurs();
    });
  }

  loadUtilisateurs(): void {
    this.utilisateurService.queryActeurs().subscribe(res => (this.utilisateurs = res.body ?? []));
  }

  save(): void {
    this.editForm.markAllAsTouched();
    if (this.editForm.invalid) return;

    this.isSaving = true;
    const entity = this.createFromForm();

    const req =
      entity.id === null ? this.service.create(entity as NewDelaiIntervention) : this.service.update(entity as IDelaiIntervention);

    req.subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.router.navigate(['/delai-intervention']);
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  previousState(): void {
    window.history.back();
  }

  protected createFromForm(): IDelaiIntervention | NewDelaiIntervention {
    const raw = this.editForm.getRawValue();
    return {
      id: raw.id ?? null,
      dateDebut: raw.dateDebut ? dayjs(raw.dateDebut) : undefined,
      dateLimite: raw.dateLimite ? dayjs(raw.dateLimite) : undefined,
      commentaire: raw.commentaire ?? undefined,
      statut: raw.statut ?? undefined,
      acteur: raw.acteur ?? null,
      degradation: raw.degradation ?? null,
    };
  }
}
