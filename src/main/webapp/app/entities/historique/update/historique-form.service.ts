import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IHistorique, NewHistorique } from '../historique.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHistorique for edit and NewHistoriqueFormGroupInput for create.
 */
type HistoriqueFormGroupInput = IHistorique | PartialWithRequiredKeyOf<NewHistorique>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IHistorique | NewHistorique> = Omit<T, 'horodatage'> & {
  horodatage?: string | null;
};

type HistoriqueFormRawValue = FormValueOf<IHistorique>;

type NewHistoriqueFormRawValue = FormValueOf<NewHistorique>;

type HistoriqueFormDefaults = Pick<NewHistorique, 'id' | 'horodatage'>;

type HistoriqueFormGroupContent = {
  id: FormControl<HistoriqueFormRawValue['id'] | NewHistorique['id']>;
  utilisateur: FormControl<HistoriqueFormRawValue['utilisateur']>;
  section: FormControl<HistoriqueFormRawValue['section']>;
  horodatage: FormControl<HistoriqueFormRawValue['horodatage']>;
  degradation: FormControl<HistoriqueFormRawValue['degradation']>;
};

export type HistoriqueFormGroup = FormGroup<HistoriqueFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HistoriqueFormService {
  createHistoriqueFormGroup(historique: HistoriqueFormGroupInput = { id: null }): HistoriqueFormGroup {
    const historiqueRawValue = this.convertHistoriqueToHistoriqueRawValue({
      ...this.getFormDefaults(),
      ...historique,
    });
    return new FormGroup<HistoriqueFormGroupContent>({
      id: new FormControl(
        { value: historiqueRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      utilisateur: new FormControl(historiqueRawValue.utilisateur),
      section: new FormControl(historiqueRawValue.section),
      horodatage: new FormControl(historiqueRawValue.horodatage),
      degradation: new FormControl(historiqueRawValue.degradation),
    });
  }

  getHistorique(form: HistoriqueFormGroup): IHistorique | NewHistorique {
    return this.convertHistoriqueRawValueToHistorique(form.getRawValue() as HistoriqueFormRawValue | NewHistoriqueFormRawValue);
  }

  resetForm(form: HistoriqueFormGroup, historique: HistoriqueFormGroupInput): void {
    const historiqueRawValue = this.convertHistoriqueToHistoriqueRawValue({ ...this.getFormDefaults(), ...historique });
    form.reset(
      {
        ...historiqueRawValue,
        id: { value: historiqueRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): HistoriqueFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      horodatage: currentTime,
    };
  }

  private convertHistoriqueRawValueToHistorique(
    rawHistorique: HistoriqueFormRawValue | NewHistoriqueFormRawValue,
  ): IHistorique | NewHistorique {
    return {
      ...rawHistorique,
      horodatage: dayjs(rawHistorique.horodatage, DATE_TIME_FORMAT),
    };
  }

  private convertHistoriqueToHistoriqueRawValue(
    historique: IHistorique | (Partial<NewHistorique> & HistoriqueFormDefaults),
  ): HistoriqueFormRawValue | PartialWithRequiredKeyOf<NewHistoriqueFormRawValue> {
    return {
      ...historique,
      horodatage: historique.horodatage ? historique.horodatage.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
