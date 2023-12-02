import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUniteAdministrative, NewUniteAdministrative } from '../unite-administrative.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUniteAdministrative for edit and NewUniteAdministrativeFormGroupInput for create.
 */
type UniteAdministrativeFormGroupInput = IUniteAdministrative | PartialWithRequiredKeyOf<NewUniteAdministrative>;

type UniteAdministrativeFormDefaults = Pick<NewUniteAdministrative, 'id'>;

type UniteAdministrativeFormGroupContent = {
  id: FormControl<IUniteAdministrative['id'] | NewUniteAdministrative['id']>;
  code: FormControl<IUniteAdministrative['code']>;
  ordre: FormControl<IUniteAdministrative['ordre']>;
  libelle: FormControl<IUniteAdministrative['libelle']>;
  typeUniteAdministrative: FormControl<IUniteAdministrative['typeUniteAdministrative']>;
  uniteAdministrativeParent: FormControl<IUniteAdministrative['uniteAdministrativeParent']>;
};

export type UniteAdministrativeFormGroup = FormGroup<UniteAdministrativeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UniteAdministrativeFormService {
  createUniteAdministrativeFormGroup(uniteAdministrative: UniteAdministrativeFormGroupInput = { id: null }): UniteAdministrativeFormGroup {
    const uniteAdministrativeRawValue = {
      ...this.getFormDefaults(),
      ...uniteAdministrative,
    };
    return new FormGroup<UniteAdministrativeFormGroupContent>({
      id: new FormControl(
        { value: uniteAdministrativeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(uniteAdministrativeRawValue.code),
      ordre: new FormControl(uniteAdministrativeRawValue.ordre),
      libelle: new FormControl(uniteAdministrativeRawValue.libelle),
      typeUniteAdministrative: new FormControl(uniteAdministrativeRawValue.typeUniteAdministrative),
      uniteAdministrativeParent: new FormControl(uniteAdministrativeRawValue.uniteAdministrativeParent),
    });
  }

  getUniteAdministrative(form: UniteAdministrativeFormGroup): IUniteAdministrative | NewUniteAdministrative {
    return form.getRawValue() as IUniteAdministrative | NewUniteAdministrative;
  }

  resetForm(form: UniteAdministrativeFormGroup, uniteAdministrative: UniteAdministrativeFormGroupInput): void {
    const uniteAdministrativeRawValue = { ...this.getFormDefaults(), ...uniteAdministrative };
    form.reset(
      {
        ...uniteAdministrativeRawValue,
        id: { value: uniteAdministrativeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UniteAdministrativeFormDefaults {
    return {
      id: null,
    };
  }
}
