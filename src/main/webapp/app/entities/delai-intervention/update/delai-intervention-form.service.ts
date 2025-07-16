import { Injectable } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import dayjs from 'dayjs';
import { IDelaiIntervention, NewDelaiIntervention, StatutDelaiIntervention } from '../delai-intervention.model';

@Injectable({ providedIn: 'root' })
export class DelaiInterventionFormService {
  constructor(private fb: FormBuilder) {}

  /**
   * Création du formulaire
   */
  createDelaiInterventionFormGroup(delai?: IDelaiIntervention | NewDelaiIntervention): FormGroup {
    const delaiRawValue = {
      ...this.getFormDefaults(),
      ...delai,
    };

    return this.fb.group({
      id: [delaiRawValue.id],
      dateDebut: [delaiRawValue.dateDebut ? dayjs(delaiRawValue.dateDebut).format('YYYY-MM-DDTHH:mm') : null, Validators.required],
      dateLimite: [delaiRawValue.dateLimite ? dayjs(delaiRawValue.dateLimite).format('YYYY-MM-DDTHH:mm') : null, Validators.required],
      commentaire: [delaiRawValue.commentaire, [Validators.required, Validators.minLength(5)]],
      statut: [delaiRawValue.statut ?? StatutDelaiIntervention.EN_COURS],
      acteur: [delaiRawValue.acteur, Validators.required],
    });
  }

  /**
   * Récupération de l'objet depuis le formulaire
   */
  getDelaiIntervention(form: FormGroup): IDelaiIntervention | NewDelaiIntervention {
    const rawValue = form.getRawValue();
    return {
      id: rawValue.id ?? null,
      dateDebut: rawValue.dateDebut ? dayjs(rawValue.dateDebut) : undefined,
      dateLimite: rawValue.dateLimite ? dayjs(rawValue.dateLimite) : undefined,
      commentaire: rawValue.commentaire ?? null,
      statut: rawValue.statut ?? StatutDelaiIntervention.EN_COURS,
      acteur: rawValue.acteur ?? null,
    };
  }

  /**
   * Réinitialisation du formulaire avec de nouvelles données
   */
  resetForm(form: FormGroup, delai: IDelaiIntervention | NewDelaiIntervention): void {
    const delaiRawValue = {
      ...this.getFormDefaults(),
      ...delai,
    };

    form.reset({
      id: delaiRawValue.id ?? null,
      dateDebut: delaiRawValue.dateDebut ? dayjs(delaiRawValue.dateDebut).format('YYYY-MM-DDTHH:mm') : null,
      dateLimite: delaiRawValue.dateLimite ? dayjs(delaiRawValue.dateLimite).format('YYYY-MM-DDTHH:mm') : null,
      commentaire: delaiRawValue.commentaire ?? '',
      statut: delaiRawValue.statut ?? StatutDelaiIntervention.EN_COURS,
      acteur: delaiRawValue.acteur ?? null,
    });
  }

  /**
   * Valeurs par défaut pour la création
   */
  private getFormDefaults(): NewDelaiIntervention {
    return {
      id: null,
      dateDebut: null,
      dateLimite: null,
      commentaire: '',
      statut: StatutDelaiIntervention.EN_COURS,
      acteur: null,
    };
  }
}
