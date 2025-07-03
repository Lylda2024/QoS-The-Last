import * as XLSX from 'xlsx';
import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { DegradationService } from '../service/degradation.service';
import { IDegradation } from '../degradation.model';
import { DegradationFormGroup, DegradationFormService } from './degradation-form.service';

@Component({
  selector: 'jhi-degradation-update',
  templateUrl: './degradation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DegradationUpdateComponent implements OnInit {
  isSaving = false;
  degradation: IDegradation | null = null;

  utilisateursSharedCollection: IUtilisateur[] = [];
  sitesSharedCollection: ISite[] = [];

  protected degradationService = inject(DegradationService);
  protected degradationFormService = inject(DegradationFormService);
  protected utilisateurService = inject(UtilisateurService);
  protected siteService = inject(SiteService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DegradationFormGroup = this.degradationFormService.createDegradationFormGroup();

  compareUtilisateur = (o1: IUtilisateur | null, o2: IUtilisateur | null): boolean => this.utilisateurService.compareUtilisateur(o1, o2);

  compareSite = (o1: ISite | null, o2: ISite | null): boolean => this.siteService.compareSite(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ degradation }) => {
      this.degradation = degradation;
      if (degradation) {
        this.updateForm(degradation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const degradation = this.degradationFormService.getDegradation(this.editForm);
    if (degradation.id !== null) {
      this.subscribeToSaveResponse(this.degradationService.update(degradation));
    } else {
      this.subscribeToSaveResponse(this.degradationService.create(degradation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDegradation>>): void {
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

  protected updateForm(degradation: IDegradation): void {
    this.degradation = degradation;
    this.degradationFormService.resetForm(this.editForm, degradation);

    this.utilisateursSharedCollection = this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(
      this.utilisateursSharedCollection,
      degradation.utilisateur,
    );
    this.sitesSharedCollection = this.siteService.addSiteToCollectionIfMissing<ISite>(this.sitesSharedCollection, degradation.site);
  }

  protected loadRelationshipsOptions(): void {
    this.utilisateurService
      .query()
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(utilisateurs, this.degradation?.utilisateur),
        ),
      )
      .subscribe((utilisateurs: IUtilisateur[]) => (this.utilisateursSharedCollection = utilisateurs));

    this.siteService
      .query()
      .pipe(map((res: HttpResponse<ISite[]>) => res.body ?? []))
      .pipe(map((sites: ISite[]) => this.siteService.addSiteToCollectionIfMissing<ISite>(sites, this.degradation?.site)))
      .subscribe((sites: ISite[]) => (this.sitesSharedCollection = sites));
  }
}
