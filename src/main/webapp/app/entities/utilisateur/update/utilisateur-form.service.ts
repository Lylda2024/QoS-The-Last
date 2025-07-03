import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IUtilisateur, NewUtilisateur } from '../utilisateur.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUtilisateur for edit and NewUtilisateurFormGroupInput for create.
 */
type UtilisateurFormGroupInput = IUtilisateur | PartialWithRequiredKeyOf<NewUtilisateur>;

type UtilisateurFormDefaults = Pick<NewUtilisateur, 'id' | 'roles'>;

type UtilisateurFormGroupContent = {
  id: FormControl<IUtilisateur['id'] | NewUtilisateur['id']>;
  nom: FormControl<IUtilisateur['nom']>;
  prenom: FormControl<IUtilisateur['prenom']>;
  email: FormControl<IUtilisateur['email']>;
  motDePasse: FormControl<IUtilisateur['motDePasse']>;
  typeUtilisateur: FormControl<IUtilisateur['typeUtilisateur']>;
  roles: FormControl<IUtilisateur['roles']>;
};

export type UtilisateurFormGroup = FormGroup<UtilisateurFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UtilisateurFormService {
  createUtilisateurFormGroup(utilisateur: UtilisateurFormGroupInput = { id: null }): UtilisateurFormGroup {
    const utilisateurRawValue = {
      ...this.getFormDefaults(),
      ...utilisateur,
    };
    return new FormGroup<UtilisateurFormGroupContent>({
      id: new FormControl(
        { value: utilisateurRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(utilisateurRawValue.nom),
      prenom: new FormControl(utilisateurRawValue.prenom),
      email: new FormControl(utilisateurRawValue.email),
      motDePasse: new FormControl(utilisateurRawValue.motDePasse),
      typeUtilisateur: new FormControl(utilisateurRawValue.typeUtilisateur),
      roles: new FormControl(utilisateurRawValue.roles ?? []),
    });
  }

  getUtilisateur(form: UtilisateurFormGroup): IUtilisateur | NewUtilisateur {
    return form.getRawValue() as IUtilisateur | NewUtilisateur;
  }

  resetForm(form: UtilisateurFormGroup, utilisateur: UtilisateurFormGroupInput): void {
    const utilisateurRawValue = { ...this.getFormDefaults(), ...utilisateur };
    form.reset(
      {
        ...utilisateurRawValue,
        id: { value: utilisateurRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UtilisateurFormDefaults {
    return {
      id: null,
      roles: [],
    };
  }
}
