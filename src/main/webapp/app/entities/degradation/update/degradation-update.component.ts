import { Component, OnInit, AfterViewInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { getActiveDelai } from 'app/entities/degradation/degradation.model';

import { IDelaiIntervention } from 'app/entities/delai-intervention/delai-intervention.model';
import { IDegradation, NewDegradation } from '../degradation.model';
import { DegradationService } from '../service/degradation.service';
import { DegradationFormService, DegradationFormGroup } from './degradation-form.service';

import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';

import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';

import { ExcelLoaderService } from 'app/services/excel-loader.service';
import { MapService } from 'app/services/map.service';

import { TypeAnomalie } from 'app/entities/enumerations/type-anomalie.model';
import { Priorite } from 'app/entities/enumerations/priorite.model';
import { Statut } from 'app/entities/enumerations/statut.model';

@Component({
  selector: 'jhi-degradation-update',
  templateUrl: './degradation-update.component.html',
  styleUrls: ['./degradation-update.component.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, FontAwesomeModule],
})
export class DegradationUpdateComponent implements OnInit, AfterViewInit {
  /* ------------------------------------------------------------------ */
  /*  Données & collections                                             */
  /* ------------------------------------------------------------------ */
  communes: string[] = [];
  filteredCommunes: string[] = [];
  suggestionsVisible = false;

  isSaving = false;
  degradation: IDegradation | null = null;
  currentStep = 1;

  typeAnomalieValues = Object.values(TypeAnomalie);
  prioriteValues = Object.values(Priorite);
  statutValues = Object.values(Statut);

  sitesSharedCollection: ISite[] = [];
  utilisateursSharedCollection: IUtilisateur[] = [];
  filteredSites: ISite[] = [];
  siteNom = '';

  /* ------------------------------------------------------------------ */
  /*  Injection                                                           */
  /* ------------------------------------------------------------------ */
  private degradationService = inject(DegradationService);
  private degradationFormService = inject(DegradationFormService);
  private siteService = inject(SiteService);
  private utilisateurService = inject(UtilisateurService);
  private activatedRoute = inject(ActivatedRoute);
  private mapService = inject(MapService);
  private excelService = inject(ExcelLoaderService);
  private cd = inject(ChangeDetectorRef);

  /* ------------------------------------------------------------------ */
  /*  Formulaire                                                          */
  /* ------------------------------------------------------------------ */
  editForm: DegradationFormGroup = this.degradationFormService.createDegradationFormGroup();

  /* ------------------------------------------------------------------ */
  /*  Mode prolongation                                                   */
  /* ------------------------------------------------------------------ */
  prolongationMode = false;

  /* ------------------------------------------------------------------ */
  /*  Lifecycle                                                           */
  /* ------------------------------------------------------------------ */
  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ degradation }) => {
      this.degradation = degradation;
      if (degradation) this.updateForm(degradation);
      this.loadRelationshipsOptions();
    });
    this.loadCommunes();
  }

  ngAfterViewInit(): void {
    setTimeout(() => this.cd.detectChanges());
  }

  /* ------------------------------------------------------------------ */
  /*  Chargement des données                                              */
  /* ------------------------------------------------------------------ */
  private loadCommunes(): void {
    this.excelService.loadCommunesFromExcel().then(communes => {
      this.communes = communes;
    });
  }

  private loadRelationshipsOptions(): void {
    this.siteService
      .getAllWithoutPagination()
      .pipe(map((sites: ISite[]) => this.siteService.addSiteToCollectionIfMissing(sites, this.degradation?.site)))
      .subscribe((sites: ISite[]) => (this.sitesSharedCollection = sites));

    this.utilisateurService
      .query()
      .pipe(
        map(res => res.body ?? []),
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing(
            utilisateurs,
            this.degradation?.porteur ? utilisateurs.find(u => u.id === Number(this.degradation!.porteur)) : undefined,
          ),
        ),
      )
      .subscribe((utilisateurs: IUtilisateur[]) => (this.utilisateursSharedCollection = utilisateurs));
  }

  /* ------------------------------------------------------------------ */
  /*  Navigation & validation                                             */
  /* ------------------------------------------------------------------ */
  goToStep(step: number): void {
    if (step === 2 && this.partie1FormIsValid()) {
      this.currentStep = 2;
    } else if (step === 1) {
      this.currentStep = 1;
    }
  }

  partie1FormIsValid(): boolean {
    const f = this.editForm.controls;
    return !!(
      f.localite?.valid &&
      f.contactTemoin?.valid &&
      f.typeAnomalie?.valid &&
      f.priorite?.valid &&
      f.actionsEffectuees?.valid &&
      f.dateDetection?.valid
    );
  }

  isInvalid(controlName: string): boolean {
    const control = this.editForm.get(controlName);
    return !!(control && control.invalid && (control.dirty || control.touched));
  }

  previousState(): void {
    window.history.back();
  }

  /* ------------------------------------------------------------------ */
  /*  Sauvegarde                                                          */
  /* ------------------------------------------------------------------ */
  save(): void {
    if (this.editForm.invalid) {
      this.editForm.markAllAsTouched();
      alert('❌ Veuillez remplir tous les champs obligatoires.');
      return;
    }
    this.isSaving = true;
    const degradation = this.degradationFormService.getDegradation(this.editForm);
    const operation: Observable<HttpResponse<IDegradation>> =
      degradation.id != null ? this.degradationService.update(degradation) : this.degradationService.create({ ...degradation, id: null });
    this.subscribeToSaveResponse(operation);
  }

  private subscribeToSaveResponse(result: Observable<HttpResponse<IDegradation>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: err => this.onSaveError(err),
    });
  }

  private onSaveSuccess(): void {
    this.previousState();
  }

  private onSaveError(error: any): void {
    this.isSaving = false;
    console.error('❌ Erreur lors de la sauvegarde :', error);
    alert('Erreur lors de la sauvegarde.');
  }

  private onSaveFinalize(): void {
    this.isSaving = false;
  }

  /* ------------------------------------------------------------------ */
  /*  Mise à jour du formulaire                                           */
  /* ------------------------------------------------------------------ */
  protected updateForm(degradation: IDegradation): void {
    this.editForm.patchValue({
      id: degradation.id,
      localite: degradation.localite,
      contactTemoin: degradation.contactTemoin,
      typeAnomalie: degradation.typeAnomalie,
      priorite: degradation.priorite,
      porteur: degradation.porteur,
      porteur2: degradation.porteur2,
      statut: degradation.statut,
      actionsEffectuees: degradation.actionsEffectuees,
      dateDetection: degradation.dateDetection ?? null,
      dateLimite: degradation.dateLimite ?? null,
      commentaire: degradation.commentaire,
      nextStep: degradation.nextStep,
      ticketOceane: degradation.ticketOceane,
      site: degradation.site,
    });
    this.siteNom = degradation.site?.nomSite ?? '';
  }

  /* ------------------------------------------------------------------ */
  /*  Gestion site & localité                                             */
  /* ------------------------------------------------------------------ */
  handleSiteInput(value: string): void {
    this.siteNom = value;
    this.onSiteInputChange(value);
  }

  onSiteInputChange(value: string): void {
    if (!value || value.length < 2) {
      this.filteredSites = [];
      return;
    }
    const searchTerm = value.toLowerCase();
    this.filteredSites = this.sitesSharedCollection.filter(site => site.nomSite?.toLowerCase().includes(searchTerm));
  }

  selectSiteSuggestion(site: ISite): void {
    this.editForm.get('site')?.setValue(site);
    this.filteredSites = [];
    this.siteNom = site.nomSite ?? '';
  }

  onLocaliteInput(value: string): void {
    const val = value.toLowerCase();
    this.filteredCommunes = this.communes.filter(c => c.toLowerCase().includes(val));
    this.suggestionsVisible = true;
  }

  selectLocalite(value: string): void {
    this.editForm.get('localite')?.setValue(value);
    this.suggestionsVisible = false;
  }

  hideSuggestions(): void {
    setTimeout(() => (this.suggestionsVisible = false), 200);
  }

  showSuggestions(): void {
    const val = this.editForm.get('localite')?.value || '';
    this.filteredCommunes = this.communes.filter(c => c.toLowerCase().includes(val.toLowerCase()));
    this.suggestionsVisible = true;
  }

  onLocaliteClicked(): void {
    const site = this.editForm.get('site')?.value;
    const priorite = this.editForm.get('priorite')?.value;
    if (!site) {
      console.warn('Site manquant');
      return;
    }
    const lat = site.latitude;
    const lng = site.longitude;
    if (lat != null && lng != null && !isNaN(lat) && !isNaN(lng)) {
      this.mapService.setLocation({ latitude: lat, longitude: lng, priorite: priorite ?? undefined });
    } else {
      console.warn('⚠️ Coordonnées invalides pour le site');
    }
  }

  /* ------------------------------------------------------------------ */
  /*  Délai                                                               */
  /* ------------------------------------------------------------------ */
  getActiveDelai(): IDelaiIntervention | null {
    if (!this.degradation || !this.degradation.delais) return null;
    return (
      this.degradation.delais
        .filter(d => d.statut !== 'TERMINE')
        .sort((a, b) => (b.dateDebut?.toDate().getTime() ?? 0) - (a.dateDebut?.toDate().getTime() ?? 0))[0] ?? null
    );
  }

  /* ------------------------------------------------------------------ */
  /*  Mode prolongation                                                   */
  /* ------------------------------------------------------------------ */
  activerProlongation(): void {
    this.prolongationMode = true;
    const currentFin = this.editForm.get('dateLimite')?.value;
    this.editForm.get('dateLimite')?.setValue(currentFin ?? null);
  }

  /* ------------------------------------------------------------------ */
  /*  Accès rapide pour le template                                       */
  /* ------------------------------------------------------------------ */
  get showDelaiBloc(): boolean {
    return !!this.degradation?.id && this.prolongationMode;
  }
}
