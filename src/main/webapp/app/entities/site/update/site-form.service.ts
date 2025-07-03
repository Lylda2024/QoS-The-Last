import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISite, NewSite } from '../site.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISite for edit and NewSiteFormGroupInput for create.
 */
type SiteFormGroupInput = ISite | PartialWithRequiredKeyOf<NewSite>;

type SiteFormDefaults = Pick<NewSite, 'id' | 'enService'>;

type SiteFormGroupContent = {
  id: FormControl<ISite['id'] | NewSite['id']>;
  nomSite: FormControl<ISite['nomSite']>;
  codeOCI: FormControl<ISite['codeOCI']>;
  longitude: FormControl<ISite['longitude']>;
  latitude: FormControl<ISite['latitude']>;
  gestionnaire: FormControl<ISite['gestionnaire']>;
  proprietaire: FormControl<ISite['proprietaire']>;
  equipementier: FormControl<ISite['equipementier']>;
  typeSite: FormControl<ISite['typeSite']>;
  modeAntenne: FormControl<ISite['modeAntenne']>;
  statut: FormControl<ISite['statut']>;
  couche: FormControl<ISite['couche']>;
  enService: FormControl<ISite['enService']>;
  technologie: FormControl<ISite['technologie']>;
  dateMiseEnService: FormControl<ISite['dateMiseEnService']>;
  dateMes2G: FormControl<ISite['dateMes2G']>;
  dateMes3G: FormControl<ISite['dateMes3G']>;
};

export type SiteFormGroup = FormGroup<SiteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SiteFormService {
  createSiteFormGroup(site: SiteFormGroupInput = { id: null }): SiteFormGroup {
    const siteRawValue = {
      ...this.getFormDefaults(),
      ...site,
    };
    return new FormGroup<SiteFormGroupContent>({
      id: new FormControl(
        { value: siteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nomSite: new FormControl(siteRawValue.nomSite),
      codeOCI: new FormControl(siteRawValue.codeOCI),
      longitude: new FormControl(siteRawValue.longitude),
      latitude: new FormControl(siteRawValue.latitude),
      gestionnaire: new FormControl(siteRawValue.gestionnaire),
      proprietaire: new FormControl(siteRawValue.proprietaire),
      equipementier: new FormControl(siteRawValue.equipementier),
      typeSite: new FormControl(siteRawValue.typeSite),
      modeAntenne: new FormControl(siteRawValue.modeAntenne),
      statut: new FormControl(siteRawValue.statut),
      couche: new FormControl(siteRawValue.couche),
      enService: new FormControl(siteRawValue.enService),
      technologie: new FormControl(siteRawValue.technologie),
      dateMiseEnService: new FormControl(siteRawValue.dateMiseEnService),
      dateMes2G: new FormControl(siteRawValue.dateMes2G),
      dateMes3G: new FormControl(siteRawValue.dateMes3G),
    });
  }

  getSite(form: SiteFormGroup): ISite | NewSite {
    return form.getRawValue() as ISite | NewSite;
  }

  resetForm(form: SiteFormGroup, site: SiteFormGroupInput): void {
    const siteRawValue = { ...this.getFormDefaults(), ...site };
    form.reset(
      {
        ...siteRawValue,
        id: { value: siteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SiteFormDefaults {
    return {
      id: null,
      enService: false,
    };
  }
}
