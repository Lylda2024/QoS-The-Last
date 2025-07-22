import { Injectable } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import dayjs from 'dayjs';
import { IDelaiIntervention, NewDelaiIntervention, StatutDelaiIntervention } from '../delai-intervention.model';

@Injectable({ providedIn: 'root' })
export class DelaiInterventionFormService {
  constructor(private fb: FormBuilder) {}

  createDelaiInterventionFormGroup(delai?: IDelaiIntervention | NewDelaiIntervention): FormGroup {
    const delaiRawValue = { ...this.getFormDefaults(), ...delai };
    return this.fb.group({
      id: [delaiRawValue.id],
      dateDebut: [delaiRawValue.dateDebut ? dayjs(delaiRawValue.dateDebut).format('YYYY-MM-DDTHH:mm') : null, Validators.required],
      dateLimite: [delaiRawValue.dateLimite ? dayjs(delaiRawValue.dateLimite).format('YYYY-MM-DDTHH:mm') : null, Validators.required],
      commentaire: [delaiRawValue.commentaire, [Validators.required, Validators.minLength(5)]],
      statut: [delaiRawValue.statut ?? StatutDelaiIntervention.EN_COURS],
      acteur: [delaiRawValue.acteur, Validators.required],
      degradation: [delaiRawValue.degradation ?? null], // contrôle caché
    });
  }

  getDelaiIntervention(form: FormGroup): IDelaiIntervention | NewDelaiIntervention {
    const raw = form.getRawValue();
    return {
      id: raw.id ?? null,
      dateDebut: raw.dateDebut ? dayjs(raw.dateDebut) : undefined,
      dateLimite: raw.dateLimite ? dayjs(raw.dateLimite) : undefined,
      commentaire: raw.commentaire ?? null,
      statut: raw.statut ?? StatutDelaiIntervention.EN_COURS,
      acteur: raw.acteur ?? null,
      degradation: raw.degradation ?? null,
    };
  }

  resetForm(form: FormGroup, delai: IDelaiIntervention | NewDelaiIntervention): void {
    const delaiRawValue = { ...this.getFormDefaults(), ...delai };
    form.reset({
      id: delaiRawValue.id ?? null,
      dateDebut: delaiRawValue.dateDebut ? dayjs(delaiRawValue.dateDebut).format('YYYY-MM-DDTHH:mm') : null,
      dateLimite: delaiRawValue.dateLimite ? dayjs(delaiRawValue.dateLimite).format('YYYY-MM-DDTHH:mm') : null,
      commentaire: delaiRawValue.commentaire ?? '',
      statut: delaiRawValue.statut ?? StatutDelaiIntervention.EN_COURS,
      acteur: delaiRawValue.acteur ?? null,
      degradation: delaiRawValue.degradation ?? null,
    });
  }

  private getFormDefaults(): NewDelaiIntervention {
    return {
      id: null,
      dateDebut: null,
      dateLimite: null,
      commentaire: '',
      statut: StatutDelaiIntervention.EN_COURS,
      acteur: null,
      degradation: null,
    };
  }
}
