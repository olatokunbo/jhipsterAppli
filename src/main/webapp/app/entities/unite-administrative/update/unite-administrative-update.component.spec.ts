import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IValeur } from 'app/entities/valeur/valeur.model';
import { ValeurService } from 'app/entities/valeur/service/valeur.service';
import { UniteAdministrativeService } from '../service/unite-administrative.service';
import { IUniteAdministrative } from '../unite-administrative.model';
import { UniteAdministrativeFormService } from './unite-administrative-form.service';

import { UniteAdministrativeUpdateComponent } from './unite-administrative-update.component';

describe('UniteAdministrative Management Update Component', () => {
  let comp: UniteAdministrativeUpdateComponent;
  let fixture: ComponentFixture<UniteAdministrativeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let uniteAdministrativeFormService: UniteAdministrativeFormService;
  let uniteAdministrativeService: UniteAdministrativeService;
  let valeurService: ValeurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), UniteAdministrativeUpdateComponent],
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
      .overrideTemplate(UniteAdministrativeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UniteAdministrativeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    uniteAdministrativeFormService = TestBed.inject(UniteAdministrativeFormService);
    uniteAdministrativeService = TestBed.inject(UniteAdministrativeService);
    valeurService = TestBed.inject(ValeurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call UniteAdministrative query and add missing value', () => {
      const uniteAdministrative: IUniteAdministrative = { id: 456 };
      const uniteAdministrativeParent: IUniteAdministrative = { id: 18808 };
      uniteAdministrative.uniteAdministrativeParent = uniteAdministrativeParent;

      const uniteAdministrativeCollection: IUniteAdministrative[] = [{ id: 25354 }];
      jest.spyOn(uniteAdministrativeService, 'query').mockReturnValue(of(new HttpResponse({ body: uniteAdministrativeCollection })));
      const additionalUniteAdministratives = [uniteAdministrativeParent];
      const expectedCollection: IUniteAdministrative[] = [...additionalUniteAdministratives, ...uniteAdministrativeCollection];
      jest.spyOn(uniteAdministrativeService, 'addUniteAdministrativeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ uniteAdministrative });
      comp.ngOnInit();

      expect(uniteAdministrativeService.query).toHaveBeenCalled();
      expect(uniteAdministrativeService.addUniteAdministrativeToCollectionIfMissing).toHaveBeenCalledWith(
        uniteAdministrativeCollection,
        ...additionalUniteAdministratives.map(expect.objectContaining),
      );
      expect(comp.uniteAdministrativesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Valeur query and add missing value', () => {
      const uniteAdministrative: IUniteAdministrative = { id: 456 };
      const typeUniteAdministrative: IValeur = { id: 24424 };
      uniteAdministrative.typeUniteAdministrative = typeUniteAdministrative;

      const valeurCollection: IValeur[] = [{ id: 8333 }];
      jest.spyOn(valeurService, 'query').mockReturnValue(of(new HttpResponse({ body: valeurCollection })));
      const additionalValeurs = [typeUniteAdministrative];
      const expectedCollection: IValeur[] = [...additionalValeurs, ...valeurCollection];
      jest.spyOn(valeurService, 'addValeurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ uniteAdministrative });
      comp.ngOnInit();

      expect(valeurService.query).toHaveBeenCalled();
      expect(valeurService.addValeurToCollectionIfMissing).toHaveBeenCalledWith(
        valeurCollection,
        ...additionalValeurs.map(expect.objectContaining),
      );
      expect(comp.valeursSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const uniteAdministrative: IUniteAdministrative = { id: 456 };
      const uniteAdministrativeParent: IUniteAdministrative = { id: 8476 };
      uniteAdministrative.uniteAdministrativeParent = uniteAdministrativeParent;
      const typeUniteAdministrative: IValeur = { id: 11060 };
      uniteAdministrative.typeUniteAdministrative = typeUniteAdministrative;

      activatedRoute.data = of({ uniteAdministrative });
      comp.ngOnInit();

      expect(comp.uniteAdministrativesSharedCollection).toContain(uniteAdministrativeParent);
      expect(comp.valeursSharedCollection).toContain(typeUniteAdministrative);
      expect(comp.uniteAdministrative).toEqual(uniteAdministrative);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUniteAdministrative>>();
      const uniteAdministrative = { id: 123 };
      jest.spyOn(uniteAdministrativeFormService, 'getUniteAdministrative').mockReturnValue(uniteAdministrative);
      jest.spyOn(uniteAdministrativeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ uniteAdministrative });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: uniteAdministrative }));
      saveSubject.complete();

      // THEN
      expect(uniteAdministrativeFormService.getUniteAdministrative).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(uniteAdministrativeService.update).toHaveBeenCalledWith(expect.objectContaining(uniteAdministrative));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUniteAdministrative>>();
      const uniteAdministrative = { id: 123 };
      jest.spyOn(uniteAdministrativeFormService, 'getUniteAdministrative').mockReturnValue({ id: null });
      jest.spyOn(uniteAdministrativeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ uniteAdministrative: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: uniteAdministrative }));
      saveSubject.complete();

      // THEN
      expect(uniteAdministrativeFormService.getUniteAdministrative).toHaveBeenCalled();
      expect(uniteAdministrativeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUniteAdministrative>>();
      const uniteAdministrative = { id: 123 };
      jest.spyOn(uniteAdministrativeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ uniteAdministrative });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(uniteAdministrativeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUniteAdministrative', () => {
      it('Should forward to uniteAdministrativeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(uniteAdministrativeService, 'compareUniteAdministrative');
        comp.compareUniteAdministrative(entity, entity2);
        expect(uniteAdministrativeService.compareUniteAdministrative).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
