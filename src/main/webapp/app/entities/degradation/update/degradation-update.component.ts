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

  protected degradationService = inject(DegradationService);
  protected degradationFormService = inject(DegradationFormService);
  protected siteService = inject(SiteService);
  protected utilisateurService = inject(UtilisateurService);
  protected activatedRoute = inject(ActivatedRoute);
  protected mapService = inject(MapService);
  protected excelService = inject(ExcelLoaderService);

  editForm: DegradationFormGroup = this.degradationFormService.createDegradationFormGroup();

  constructor(private cd: ChangeDetectorRef) {}

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

  private loadCommunes(): void {
    this.excelService.loadCommunesFromExcel().then(communes => {
      this.communes = communes;
    });
  }

  get siteNom(): string {
    const site = this.editForm.get('site')?.value;
    return typeof site === 'object' && site?.nomSite ? site.nomSite : '';
  }

  goToStep(step: number): void {
    if (step === 2 && this.partie1FormIsValid()) {
      this.currentStep = 2;
    } else if (step === 1) {
      this.currentStep = 1;
    }
  }

  partie1FormIsValid(): boolean {
    return (
      this.editForm.get('priorite')?.valid === true &&
      this.editForm.get('localite')?.valid === true &&
      this.editForm.get('contactTemoin')?.valid === true &&
      this.editForm.get('typeAnomalie')?.valid === true &&
      this.editForm.get('actionsEffectuees')?.valid === true &&
      this.editForm.get('dateDetection')?.valid === true
    );
  }

  isInvalid(controlName: string): boolean {
    const control = this.editForm.get(controlName);
    return !!(control && control.invalid && (control.dirty || control.touched));
  }

  handleSiteInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.onSiteInputChange(input.value);
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
  }

  onLocaliteInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const val = input?.value ?? '';
    this.filteredCommunes = this.communes.filter(c => c.toLowerCase().includes(val.toLowerCase()));
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
    const input = this.editForm.get('localite')?.value || '';
    this.filteredCommunes = this.communes.filter(c => c.toLowerCase().includes(input.toLowerCase()));
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
      console.log('📍 Localisation envoyée au MapService :', { lat, lng, priorite });
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

    console.log('✅ Dégradation à sauvegarder :', degradation);

    if (degradation.id != null) {
      console.log('🔄 Mise à jour de la dégradation ID:', degradation.id);
      this.subscribeToSaveResponse(this.degradationService.update(degradation));
    } else {
      const newDegradation: NewDegradation = {
        ...degradation,
        id: null,
      };
      console.log('➕ Création d’une nouvelle dégradation');
      this.subscribeToSaveResponse(this.degradationService.create(newDegradation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDegradation>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: err => this.onSaveError(err),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }
  protected onSaveError(error: any): void {
    this.isSaving = false;
    console.error('❌ Erreur lors de la sauvegarde :', error);

    if (error.status === 400) {
      alert('Erreur 400 : Requête invalide. Vérifiez les champs obligatoires.');
    } else if (error.status === 500) {
      alert('Erreur 500 : Erreur interne du serveur.');
    } else {
      alert('Erreur inconnue lors de la sauvegarde.');
    }
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(degradation: IDegradation): void {
    this.degradation = degradation;
    this.degradationFormService.resetForm(this.editForm, degradation);
  }

  protected loadRelationshipsOptions(): void {
    this.siteService
      .getAllWithoutPagination()
      .pipe(map((sites: ISite[]) => this.siteService.addSiteToCollectionIfMissing(sites, this.degradation?.site)))
      .subscribe((sites: ISite[]) => (this.sitesSharedCollection = sites));

    this.utilisateurService
      .query()
      .pipe(
        map(res => res.body ?? []),
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing(utilisateurs, this.degradation?.porteur as IUtilisateur | undefined),
        ),
      )
      .subscribe((utilisateurs: IUtilisateur[]) => (this.utilisateursSharedCollection = utilisateurs));
  }
}
