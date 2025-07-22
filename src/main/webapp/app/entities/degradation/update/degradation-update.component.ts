import { Component, OnInit, AfterViewInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDegradation, NewDegradation, DegradationFormGroup } from '../degradation.model';
import { DegradationService } from '../service/degradation.service';
import { DegradationFormService } from './degradation-form.service';

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
  /* --- variables --- */
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

  siteNom = ''; // simple property, no getter

  /* --- injections --- */
  private degradationService = inject(DegradationService);
  private degradationFormService = inject(DegradationFormService);
  private siteService = inject(SiteService);
  private utilisateurService = inject(UtilisateurService);
  private activatedRoute = inject(ActivatedRoute);
  private mapService = inject(MapService);
  private excelService = inject(ExcelLoaderService);
  private cd = inject(ChangeDetectorRef);

  /* --- form --- */
  editForm: DegradationFormGroup = this.degradationFormService.createDegradationFormGroup();

  /* --- lifecycle --- */
  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ degradation }) => {
      this.degradation = degradation;
      if (degradation) {
        this.updateForm(degradation);
      }
      this.loadRelationshipsOptions();
    });
    this.loadCommunes();
  }

  ngAfterViewInit(): void {
    setTimeout(() => this.cd.detectChanges());
  }

  /* --- communes --- */
  private loadCommunes(): void {
    this.excelService.loadCommunesFromExcel().then(communes => {
      this.communes = communes;
    });
  }

  /* --- navigation --- */
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

  /* --- site autocomplete --- */
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

  /* --- localite autocomplete --- */
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
      this.mapService.setLocation({
        latitude: lat,
        longitude: lng,
        priorite: priorite ?? undefined,
      });
      console.log('📍 Localisation envoyée au MapService :', {
        lat,
        lng,
        priorite,
      });
    } else {
      console.warn('⚠️ Coordonnées invalides pour le site');
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    if (this.editForm.invalid) {
      this.editForm.markAllAsTouched();
      alert('❌ Veuillez remplir tous les champs obligatoires.');
      return;
    }
    this.isSaving = true;
    const degradation = this.degradationFormService.getDegradation(this.editForm);
    if (degradation.id != null) {
      this.subscribeToSaveResponse(this.degradationService.update(degradation));
    } else {
      const newDegradation: NewDegradation = { ...degradation, id: null };
      this.subscribeToSaveResponse(this.degradationService.create(newDegradation));
    }
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
    if (error.status === 400) {
      alert('Erreur 400 : Requête invalide.');
    } else if (error.status === 500) {
      alert('Erreur 500 : Erreur interne.');
    } else {
      alert('Erreur inconnue.');
    }
  }

  private onSaveFinalize(): void {
    this.isSaving = false;
  }

  private updateForm(degradation: IDegradation): void {
    this.degradation = degradation;
    this.degradationFormService.resetForm(this.editForm, {
      ...degradation,
      dateDetection: degradation.dateDetection ? new Date(degradation.dateDetection).toISOString().substring(0, 10) : '',
      dateLimite: degradation.dateLimite ? new Date(degradation.dateLimite).toISOString().substring(0, 10) : '',
      nextStep: degradation.nextStep ?? '',
      ticketOceane: degradation.ticketOceane ?? '',
      commentaire: degradation.commentaire ?? '',
      statut: degradation.statut ?? '',
      site: degradation.site ?? null,
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
            this.degradation?.porteur
              ? (utilisateurs.find(u => u.id === Number(this.degradation!.porteur)) ??
                  (this.degradation!.porteur as unknown as IUtilisateur))
              : undefined,
          ),
        ),
      )
      .subscribe((utilisateurs: IUtilisateur[]) => (this.utilisateursSharedCollection = utilisateurs));
  }
}
