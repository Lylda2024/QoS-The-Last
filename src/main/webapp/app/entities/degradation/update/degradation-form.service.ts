import { Injectable } from '@angular/core';
import { FormBuilder, Validators, FormControl, FormGroup } from '@angular/forms';
import dayjs from 'dayjs';

import { IDegradation, NewDegradation, DegradationFormGroupInput } from '../degradation.model';
import { IDelaiIntervention } from 'app/entities/delai-intervention/delai-intervention.model';
import { ISite } from 'app/entities/site/site.model';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';

/* ------------------------------------------------------------------ */
/*  Interface interne : contrôle chaque champ                           */
/* ------------------------------------------------------------------ */
export interface DegradationFormGroupContent {
  id: FormControl<number | null>;
  localite: FormControl<string>;
  contactTemoin: FormControl<string>;
  typeAnomalie: FormControl<string>;
  priorite: FormControl<string>;
  porteur: FormControl<string>;
  porteur2: FormControl<string>;
  statut: FormControl<string>;
  actionsEffectuees: FormControl<string>;
  dateDetection: FormControl<dayjs.Dayjs | null>;
  dateLimite: FormControl<dayjs.Dayjs | null>;
  commentaire: FormControl<string>;
  site: FormControl<ISite | null>;
  nextStep: FormControl<string>;
  ticketOceane: FormControl<string>;
}

/* ------------------------------------------------------------------ */
/*  Alias de type : FormGroup typé                                      */
/* ------------------------------------------------------------------ */
export type DegradationFormGroup = FormGroup<DegradationFormGroupContent>;

/* ------------------------------------------------------------------ */
/*  Service                                                             */
/* ------------------------------------------------------------------ */
@Injectable({ providedIn: 'root' })
export class DegradationFormService {
  protected resourceUrl = 'api/sites';

  constructor(
    private formBuilder: FormBuilder,
    protected http: HttpClient,
  ) {}

  /* ---------------------------------------------------------------- */
  /*  Création du FormGroup                                             */
  /* ---------------------------------------------------------------- */
  createDegradationFormGroup(degradation: DegradationFormGroupInput = { id: null }): DegradationFormGroup {
    return this.formBuilder.group({
      id: this.formBuilder.control({ value: degradation.id, disabled: true }, { nonNullable: true, validators: [Validators.required] }),
      localite: this.formBuilder.control(degradation.localite ?? '', [Validators.required]),
      contactTemoin: this.formBuilder.control(degradation.contactTemoin ?? '', [Validators.required]),
      typeAnomalie: this.formBuilder.control(degradation.typeAnomalie ?? '', [Validators.required]),
      priorite: this.formBuilder.control(degradation.priorite ?? '', [Validators.required]),
      porteur: this.formBuilder.control(degradation.porteur ?? '', [Validators.required]),
      porteur2: this.formBuilder.control(degradation.porteur2 ?? ''),
      statut: this.formBuilder.control(degradation.statut ?? ''),
      actionsEffectuees: this.formBuilder.control(degradation.actionsEffectuees ?? '', [Validators.required]),
      dateDetection: this.formBuilder.control(degradation.dateDetection ?? null, [Validators.required]),
      dateLimite: this.formBuilder.control(degradation.dateLimite ?? null),
      commentaire: this.formBuilder.control(degradation.commentaire ?? ''),
      site: this.formBuilder.control(degradation.site ?? null, [Validators.required]),
      nextStep: this.formBuilder.control(degradation.nextStep ?? ''),
      ticketOceane: this.formBuilder.control(degradation.ticketOceane ?? ''),
    }) as DegradationFormGroup;
  }

  /* ---------------------------------------------------------------- */
  /*  Extraction du modèle brut                                         */
  /* ---------------------------------------------------------------- */
  getDegradation(form: DegradationFormGroup): IDegradation | NewDegradation {
    const raw = form.getRawValue();
    return {
      ...raw,
      dateDetection: raw.dateDetection ? dayjs(raw.dateDetection) : null,
      dateLimite: raw.dateLimite ? dayjs(raw.dateLimite) : null,
    };
  }

  /* ---------------------------------------------------------------- */
  /*  Reset du formulaire                                               */
  /* ---------------------------------------------------------------- */
  resetForm(form: DegradationFormGroup, degradation: DegradationFormGroupInput): void {
    form.reset({
      ...degradation,
      id: { value: degradation.id, disabled: true },
      dateDetection: degradation.dateDetection ?? null,
      dateLimite: degradation.dateLimite ?? null,
    } as any);
  }

  /* ---------------------------------------------------------------- */
  /*  Autres méthodes                                                   */
  /* ---------------------------------------------------------------- */
  createMany(sites: ISite[]): Observable<HttpResponse<ISite[]>> {
    return this.http.post<ISite[]>(`${this.resourceUrl}/bulk`, sites, { observe: 'response' });
  }

  updateDegradationWithDelai(degradationId: number, newDelai: IDelaiIntervention): Observable<IDegradation> {
    return this.http.patch<IDegradation>(`api/degradations/${degradationId}`, {
      delais: [newDelai],
    });
  }
}
