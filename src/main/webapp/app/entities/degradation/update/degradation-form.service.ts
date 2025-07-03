import { Injectable } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { IDegradation, NewDegradation, DegradationFormGroupInput, DegradationFormGroup } from '../degradation.model';

@Injectable({ providedIn: 'root' })
export class DegradationFormService {
  constructor(private formBuilder: FormBuilder) {}

  createDegradationFormGroup(degradation: DegradationFormGroupInput = { id: null }): DegradationFormGroup {
    return this.formBuilder.group({
      id: this.formBuilder.control(
        { value: degradation.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      numero: this.formBuilder.control(degradation.numero ?? '', [Validators.required]),
      localite: this.formBuilder.control(degradation.localite ?? null),
      contactTemoin: this.formBuilder.control(degradation.contactTemoin ?? null),
      typeAnomalie: this.formBuilder.control(degradation.typeAnomalie ?? null),
      priorite: this.formBuilder.control(degradation.priorite ?? null),
      problem: this.formBuilder.control(degradation.problem ?? null),
      porteur: this.formBuilder.control(degradation.porteur ?? null),
      statut: this.formBuilder.control(degradation.statut ?? null),
      actionsEffectuees: this.formBuilder.control(degradation.actionsEffectuees ?? null),
      dateDetection: this.formBuilder.control(degradation.dateDetection ?? null),
      commentaire: this.formBuilder.control(degradation.commentaire ?? null),
      utilisateur: this.formBuilder.control(degradation.utilisateur ?? null),
      site: this.formBuilder.control(degradation.site ?? null),
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
}
