import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { AlertErrorComponent } from 'app/shared/alert/alert-error.component';
import { DegradationFormService } from './degradation-form.service';
import { DegradationFormGroup } from '../degradation.model';
import { IDegradation, NewDegradation } from '../degradation.model';
import { DegradationService } from '../service/degradation.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { TypeAnomalie } from 'app/entities/enumerations/type-anomalie.model';
import { Priorite } from 'app/entities/enumerations/priorite.model';
import { Statut } from 'app/entities/enumerations/statut.model';

import { ExcelReaderService } from 'app/services/excel-reader.service';
import { MapService } from 'app/services/map.service';

@Component({
  selector: 'jhi-degradation-update',
  templateUrl: './degradation-update.component.html',
  styleUrls: ['./degradation-update.component.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, FontAwesomeModule, AlertErrorComponent],
})
export class DegradationUpdateComponent implements OnInit {
  isSaving = false;
  degradation: IDegradation | null = null;

  typeAnomalieValues = Object.keys(TypeAnomalie);
  prioriteValues = Object.keys(Priorite);
  statutValues = Object.keys(Statut);

  utilisateursSharedCollection: IUtilisateur[] = [];
  sitesSharedCollection: ISite[] = [];

  localites: string[] = [];
  zones: { commune: string; zone: string }[] = [];
  zonesFiltered: typeof this.zones = [];

  protected degradationService = inject(DegradationService);
  protected degradationFormService = inject(DegradationFormService);
  protected utilisateurService = inject(UtilisateurService);
  protected siteService = inject(SiteService);
  protected activatedRoute = inject(ActivatedRoute);
  protected excelService = inject(ExcelReaderService);
  protected mapService = inject(MapService);

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

  onFileChange(event: any): void {
    const file = event.target.files[0];
    if (!file) {
      return;
    }
    this.excelService
      .parseFile(file)
      .then(data => {
        this.zones = data.map(row => ({
          commune: row['commune'],
          zone: row['zone'],
        }));
        this.localites = [...new Set(this.zones.map(z => z.commune))];
        this.zonesFiltered = [];
      })
      .catch(err => {
        console.error('Erreur lecture fichier Excel:', err);
      });
  }

  onLocaliteChange(commune: string): void {
    this.zonesFiltered = this.zones.filter(z => z.commune === commune);
    this.editForm.patchValue({ localite: commune });
  }

  onZoneChange(zoneName: string): void {
    const selected = this.zonesFiltered.find(z => z.zone === zoneName);
    if (selected) {
      this.editForm.patchValue({
        localite: selected.commune,
      });
    }
  }

  /**
   * Envoi la localisation du site sélectionné au service MapService
   */
  onLocaliteClicked(): void {
    if (!this.degradation || !this.degradation.site) {
      console.warn('Dégradation ou site manquant');
      return;
    }

    const lat = this.degradation.site.latitude;
    const lng = this.degradation.site.longitude;
    const priorite = this.degradation.priorite ?? undefined;

    if (lat && lng && !isNaN(lat) && !isNaN(lng)) {
      this.mapService.setLocation({
        latitude: lat,
        longitude: lng,
        priorite: priorite,
      });
      console.log('Localisation envoyée au service MapService', { lat, lng, priorite });
    } else {
      console.warn('Coordonnées invalides pour la dégradation sélectionnée');
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const degradation = this.degradationFormService.getDegradation(this.editForm);

    if (degradation.id != null) {
      this.subscribeToSaveResponse(this.degradationService.update(degradation));
      console.log('🛰️ Mise à jour envoyée :', degradation);
    } else {
      const newDegradation: NewDegradation = {
        ...degradation,
        id: null,
      };
      this.subscribeToSaveResponse(this.degradationService.create(newDegradation));
      console.log('🛰️ Création envoyée :', newDegradation);
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
    console.error('❌ Échec lors de la sauvegarde');
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(degradation: IDegradation): void {
    this.degradation = degradation;
    this.degradationFormService.resetForm(this.editForm, degradation);
  }

  protected loadRelationshipsOptions(): void {
    this.utilisateurService
      .query()
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing(utilisateurs, this.degradation?.utilisateur),
        ),
      )
      .subscribe((utilisateurs: IUtilisateur[]) => (this.utilisateursSharedCollection = utilisateurs));

    this.siteService
      .query()
      .pipe(map((res: HttpResponse<ISite[]>) => res.body ?? []))
      .pipe(map((sites: ISite[]) => this.siteService.addSiteToCollectionIfMissing(sites, this.degradation?.site)))
      .subscribe((sites: ISite[]) => (this.sitesSharedCollection = sites));
  }
}
