import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITypeUtilisateur, NewTypeUtilisateur } from '../type-utilisateur.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeUtilisateur for edit and NewTypeUtilisateurFormGroupInput for create.
 */
type TypeUtilisateurFormGroupInput = ITypeUtilisateur | PartialWithRequiredKeyOf<NewTypeUtilisateur>;

type TypeUtilisateurFormDefaults = Pick<NewTypeUtilisateur, 'id'>;

type TypeUtilisateurFormGroupContent = {
  id: FormControl<ITypeUtilisateur['id'] | NewTypeUtilisateur['id']>;
  nom: FormControl<ITypeUtilisateur['nom']>;
  description: FormControl<ITypeUtilisateur['description']>;
  niveau: FormControl<ITypeUtilisateur['niveau']>;
  permissions: FormControl<ITypeUtilisateur['permissions']>;
};

export type TypeUtilisateurFormGroup = FormGroup<TypeUtilisateurFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TypeUtilisateurFormService {
  createTypeUtilisateurFormGroup(typeUtilisateur: TypeUtilisateurFormGroupInput = { id: null }): TypeUtilisateurFormGroup {
    const typeUtilisateurRawValue = {
      ...this.getFormDefaults(),
      ...typeUtilisateur,
    };
    return new FormGroup<TypeUtilisateurFormGroupContent>({
      id: new FormControl(
        { value: typeUtilisateurRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(typeUtilisateurRawValue.nom),
      description: new FormControl(typeUtilisateurRawValue.description),
      niveau: new FormControl(typeUtilisateurRawValue.niveau),
      permissions: new FormControl(typeUtilisateurRawValue.permissions),
    });
  }

  getTypeUtilisateur(form: TypeUtilisateurFormGroup): ITypeUtilisateur | NewTypeUtilisateur {
    return form.getRawValue() as ITypeUtilisateur | NewTypeUtilisateur;
  }

  resetForm(form: TypeUtilisateurFormGroup, typeUtilisateur: TypeUtilisateurFormGroupInput): void {
    const typeUtilisateurRawValue = { ...this.getFormDefaults(), ...typeUtilisateur };
    form.reset(
      {
        ...typeUtilisateurRawValue,
        id: { value: typeUtilisateurRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TypeUtilisateurFormDefaults {
    return {
      id: null,
    };
  }
}
