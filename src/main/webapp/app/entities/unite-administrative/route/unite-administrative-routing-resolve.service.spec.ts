import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IUniteAdministrative } from '../unite-administrative.model';
import { UniteAdministrativeService } from '../service/unite-administrative.service';

import uniteAdministrativeResolve from './unite-administrative-routing-resolve.service';

describe('UniteAdministrative routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: UniteAdministrativeService;
  let resultUniteAdministrative: IUniteAdministrative | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(UniteAdministrativeService);
    resultUniteAdministrative = undefined;
  });

  describe('resolve', () => {
    it('should return IUniteAdministrative returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        uniteAdministrativeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultUniteAdministrative = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultUniteAdministrative).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        uniteAdministrativeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultUniteAdministrative = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultUniteAdministrative).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IUniteAdministrative>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        uniteAdministrativeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultUniteAdministrative = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultUniteAdministrative).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
