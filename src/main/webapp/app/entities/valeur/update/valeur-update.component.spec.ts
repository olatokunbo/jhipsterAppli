import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ValeurService } from '../service/valeur.service';
import { IValeur } from '../valeur.model';
import { ValeurFormService } from './valeur-form.service';

import { ValeurUpdateComponent } from './valeur-update.component';

describe('Valeur Management Update Component', () => {
  let comp: ValeurUpdateComponent;
  let fixture: ComponentFixture<ValeurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let valeurFormService: ValeurFormService;
  let valeurService: ValeurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ValeurUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ValeurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ValeurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    valeurFormService = TestBed.inject(ValeurFormService);
    valeurService = TestBed.inject(ValeurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Valeur query and add missing value', () => {
      const valeur: IValeur = { id: 456 };
      const valeurListFils: IValeur = { id: 22846 };
      valeur.valeurListFils = valeurListFils;

      const valeurCollection: IValeur[] = [{ id: 14748 }];
      jest.spyOn(valeurService, 'query').mockReturnValue(of(new HttpResponse({ body: valeurCollection })));
      const additionalValeurs = [valeurListFils];
      const expectedCollection: IValeur[] = [...additionalValeurs, ...valeurCollection];
      jest.spyOn(valeurService, 'addValeurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ valeur });
      comp.ngOnInit();

      expect(valeurService.query).toHaveBeenCalled();
      expect(valeurService.addValeurToCollectionIfMissing).toHaveBeenCalledWith(
        valeurCollection,
        ...additionalValeurs.map(expect.objectContaining),
      );
      expect(comp.valeursSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const valeur: IValeur = { id: 456 };
      const valeurListFils: IValeur = { id: 10865 };
      valeur.valeurListFils = valeurListFils;

      activatedRoute.data = of({ valeur });
      comp.ngOnInit();

      expect(comp.valeursSharedCollection).toContain(valeurListFils);
      expect(comp.valeur).toEqual(valeur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IValeur>>();
      const valeur = { id: 123 };
      jest.spyOn(valeurFormService, 'getValeur').mockReturnValue(valeur);
      jest.spyOn(valeurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ valeur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: valeur }));
      saveSubject.complete();

      // THEN
      expect(valeurFormService.getValeur).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(valeurService.update).toHaveBeenCalledWith(expect.objectContaining(valeur));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IValeur>>();
      const valeur = { id: 123 };
      jest.spyOn(valeurFormService, 'getValeur').mockReturnValue({ id: null });
      jest.spyOn(valeurService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ valeur: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: valeur }));
      saveSubject.complete();

      // THEN
      expect(valeurFormService.getValeur).toHaveBeenCalled();
      expect(valeurService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IValeur>>();
      const valeur = { id: 123 };
      jest.spyOn(valeurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ valeur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(valeurService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareValeur', () => {
      it('Should forward to valeurService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(valeurService, 'compareValeur');
        comp.compareValeur(entity, entity2);
        expect(valeurService.compareValeur).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
