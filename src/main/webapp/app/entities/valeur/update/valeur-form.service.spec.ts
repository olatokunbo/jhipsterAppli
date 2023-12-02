import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../valeur.test-samples';

import { ValeurFormService } from './valeur-form.service';

describe('Valeur Form Service', () => {
  let service: ValeurFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ValeurFormService);
  });

  describe('Service methods', () => {
    describe('createValeurFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createValeurFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            ordre: expect.any(Object),
            libelle: expect.any(Object),
            abreviation: expect.any(Object),
            description: expect.any(Object),
            valeurParent: expect.any(Object),
          }),
        );
      });

      it('passing IValeur should create a new form with FormGroup', () => {
        const formGroup = service.createValeurFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            ordre: expect.any(Object),
            libelle: expect.any(Object),
            abreviation: expect.any(Object),
            description: expect.any(Object),
            valeurParent: expect.any(Object),
          }),
        );
      });
    });

    describe('getValeur', () => {
      it('should return NewValeur for default Valeur initial value', () => {
        const formGroup = service.createValeurFormGroup(sampleWithNewData);

        const valeur = service.getValeur(formGroup) as any;

        expect(valeur).toMatchObject(sampleWithNewData);
      });

      it('should return NewValeur for empty Valeur initial value', () => {
        const formGroup = service.createValeurFormGroup();

        const valeur = service.getValeur(formGroup) as any;

        expect(valeur).toMatchObject({});
      });

      it('should return IValeur', () => {
        const formGroup = service.createValeurFormGroup(sampleWithRequiredData);

        const valeur = service.getValeur(formGroup) as any;

        expect(valeur).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IValeur should not enable id FormControl', () => {
        const formGroup = service.createValeurFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewValeur should disable id FormControl', () => {
        const formGroup = service.createValeurFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
