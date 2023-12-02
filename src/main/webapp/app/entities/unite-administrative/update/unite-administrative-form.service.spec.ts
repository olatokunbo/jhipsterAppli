import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../unite-administrative.test-samples';

import { UniteAdministrativeFormService } from './unite-administrative-form.service';

describe('UniteAdministrative Form Service', () => {
  let service: UniteAdministrativeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UniteAdministrativeFormService);
  });

  describe('Service methods', () => {
    describe('createUniteAdministrativeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUniteAdministrativeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            ordre: expect.any(Object),
            libelle: expect.any(Object),
            typeUniteAdministrative: expect.any(Object),
            uniteAdministrativeParent: expect.any(Object),
          }),
        );
      });

      it('passing IUniteAdministrative should create a new form with FormGroup', () => {
        const formGroup = service.createUniteAdministrativeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            ordre: expect.any(Object),
            libelle: expect.any(Object),
            typeUniteAdministrative: expect.any(Object),
            uniteAdministrativeParent: expect.any(Object),
          }),
        );
      });
    });

    describe('getUniteAdministrative', () => {
      it('should return NewUniteAdministrative for default UniteAdministrative initial value', () => {
        const formGroup = service.createUniteAdministrativeFormGroup(sampleWithNewData);

        const uniteAdministrative = service.getUniteAdministrative(formGroup) as any;

        expect(uniteAdministrative).toMatchObject(sampleWithNewData);
      });

      it('should return NewUniteAdministrative for empty UniteAdministrative initial value', () => {
        const formGroup = service.createUniteAdministrativeFormGroup();

        const uniteAdministrative = service.getUniteAdministrative(formGroup) as any;

        expect(uniteAdministrative).toMatchObject({});
      });

      it('should return IUniteAdministrative', () => {
        const formGroup = service.createUniteAdministrativeFormGroup(sampleWithRequiredData);

        const uniteAdministrative = service.getUniteAdministrative(formGroup) as any;

        expect(uniteAdministrative).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUniteAdministrative should not enable id FormControl', () => {
        const formGroup = service.createUniteAdministrativeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUniteAdministrative should disable id FormControl', () => {
        const formGroup = service.createUniteAdministrativeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
