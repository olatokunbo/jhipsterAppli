import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IValeur, NewValeur } from '../valeur.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IValeur for edit and NewValeurFormGroupInput for create.
 */
type ValeurFormGroupInput = IValeur | PartialWithRequiredKeyOf<NewValeur>;

type ValeurFormDefaults = Pick<NewValeur, 'id'>;

type ValeurFormGroupContent = {
  id: FormControl<IValeur['id'] | NewValeur['id']>;
  code: FormControl<IValeur['code']>;
  ordre: FormControl<IValeur['ordre']>;
  libelle: FormControl<IValeur['libelle']>;
  abreviation: FormControl<IValeur['abreviation']>;
  description: FormControl<IValeur['description']>;
  valeurParent: FormControl<IValeur['valeurParent']>;
};

export type ValeurFormGroup = FormGroup<ValeurFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ValeurFormService {
  createValeurFormGroup(valeur: ValeurFormGroupInput = { id: null }): ValeurFormGroup {
    const valeurRawValue = {
      ...this.getFormDefaults(),
      ...valeur,
    };
    return new FormGroup<ValeurFormGroupContent>({
      id: new FormControl(
        { value: valeurRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(valeurRawValue.code),
      ordre: new FormControl(valeurRawValue.ordre),
      libelle: new FormControl(valeurRawValue.libelle),
      abreviation: new FormControl(valeurRawValue.abreviation),
      description: new FormControl(valeurRawValue.description),
      valeurParent: new FormControl(valeurRawValue.valeurParent),
    });
  }

  getValeur(form: ValeurFormGroup): IValeur | NewValeur {
    return form.getRawValue() as IValeur | NewValeur;
  }

  resetForm(form: ValeurFormGroup, valeur: ValeurFormGroupInput): void {
    const valeurRawValue = { ...this.getFormDefaults(), ...valeur };
    form.reset(
      {
        ...valeurRawValue,
        id: { value: valeurRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ValeurFormDefaults {
    return {
      id: null,
    };
  }
}
