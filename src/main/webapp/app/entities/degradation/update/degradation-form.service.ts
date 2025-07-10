import { Injectable } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { IDegradation, NewDegradation, DegradationFormGroupInput, DegradationFormGroup } from '../degradation.model';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ISite, NewSite } from 'app/entities/site/site.model';

@Injectable({ providedIn: 'root' })
export class DegradationFormService {
  protected resourceUrl = 'api/sites';

  constructor(
    private formBuilder: FormBuilder,
    protected http: HttpClient,
  ) {}

  createDegradationFormGroup(degradation: DegradationFormGroupInput = { id: null }): DegradationFormGroup {
    return this.formBuilder.group({
      id: this.formBuilder.control(
        { value: degradation.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),

      localite: this.formBuilder.control(degradation.localite ?? '', [Validators.required]),
      contactTemoin: this.formBuilder.control(degradation.contactTemoin ?? '', [Validators.required]),
      typeAnomalie: this.formBuilder.control(degradation.typeAnomalie ?? '', [Validators.required]),
      priorite: this.formBuilder.control(degradation.priorite ?? '', [Validators.required]),
      porteur: this.formBuilder.control(degradation.porteur ?? '', [Validators.required]),
      actionsEffectuees: this.formBuilder.control(degradation.actionsEffectuees ?? '', [Validators.required]),
      dateDetection: this.formBuilder.control(degradation.dateDetection ?? '', [Validators.required]),
      commentaire: this.formBuilder.control(degradation.commentaire ?? ''),
      site: this.formBuilder.control(degradation.site ?? null, [Validators.required]),

      porteur2: this.formBuilder.control(degradation.porteur2 ?? ''),
      nextStep: this.formBuilder.control(degradation.nextStep ?? ''),
      ticketOceane: this.formBuilder.control(degradation.ticketOceane ?? ''),
      statut: this.formBuilder.control(degradation.statut ?? ''),
    }) as DegradationFormGroup;
  }

  getDegradation(form: DegradationFormGroup): IDegradation | NewDegradation {
    const raw = form.getRawValue();
    return {
      ...raw,
      dateDetection: raw.dateDetection ? new Date(raw.dateDetection) : null,
    };
  }

  resetForm(form: DegradationFormGroup, degradation: DegradationFormGroupInput): void {
    form.reset({
      ...degradation,
      id: { value: degradation.id, disabled: true },
    } as any);
  }

  createMany(sites: NewSite[]): Observable<HttpResponse<ISite[]>> {
    return this.http.post<ISite[]>(`${this.resourceUrl}/bulk`, sites, { observe: 'response' });
  }
}
