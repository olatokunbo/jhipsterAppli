import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUniteAdministrative } from '../unite-administrative.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../unite-administrative.test-samples';

import { UniteAdministrativeService } from './unite-administrative.service';

const requireRestSample: IUniteAdministrative = {
  ...sampleWithRequiredData,
};

describe('UniteAdministrative Service', () => {
  let service: UniteAdministrativeService;
  let httpMock: HttpTestingController;
  let expectedResult: IUniteAdministrative | IUniteAdministrative[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UniteAdministrativeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a UniteAdministrative', () => {
      const uniteAdministrative = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(uniteAdministrative).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UniteAdministrative', () => {
      const uniteAdministrative = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(uniteAdministrative).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UniteAdministrative', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UniteAdministrative', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UniteAdministrative', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a UniteAdministrative', () => {
      const queryObject: any = {
        page: 0,
        size: 20,
        query: '',
        sort: [],
      };
      service.search(queryObject).subscribe(() => expectedResult);

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(null, { status: 500, statusText: 'Internal Server Error' });
      expect(expectedResult).toBe(null);
    });

    describe('addUniteAdministrativeToCollectionIfMissing', () => {
      it('should add a UniteAdministrative to an empty array', () => {
        const uniteAdministrative: IUniteAdministrative = sampleWithRequiredData;
        expectedResult = service.addUniteAdministrativeToCollectionIfMissing([], uniteAdministrative);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(uniteAdministrative);
      });

      it('should not add a UniteAdministrative to an array that contains it', () => {
        const uniteAdministrative: IUniteAdministrative = sampleWithRequiredData;
        const uniteAdministrativeCollection: IUniteAdministrative[] = [
          {
            ...uniteAdministrative,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUniteAdministrativeToCollectionIfMissing(uniteAdministrativeCollection, uniteAdministrative);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UniteAdministrative to an array that doesn't contain it", () => {
        const uniteAdministrative: IUniteAdministrative = sampleWithRequiredData;
        const uniteAdministrativeCollection: IUniteAdministrative[] = [sampleWithPartialData];
        expectedResult = service.addUniteAdministrativeToCollectionIfMissing(uniteAdministrativeCollection, uniteAdministrative);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(uniteAdministrative);
      });

      it('should add only unique UniteAdministrative to an array', () => {
        const uniteAdministrativeArray: IUniteAdministrative[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const uniteAdministrativeCollection: IUniteAdministrative[] = [sampleWithRequiredData];
        expectedResult = service.addUniteAdministrativeToCollectionIfMissing(uniteAdministrativeCollection, ...uniteAdministrativeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const uniteAdministrative: IUniteAdministrative = sampleWithRequiredData;
        const uniteAdministrative2: IUniteAdministrative = sampleWithPartialData;
        expectedResult = service.addUniteAdministrativeToCollectionIfMissing([], uniteAdministrative, uniteAdministrative2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(uniteAdministrative);
        expect(expectedResult).toContain(uniteAdministrative2);
      });

      it('should accept null and undefined values', () => {
        const uniteAdministrative: IUniteAdministrative = sampleWithRequiredData;
        expectedResult = service.addUniteAdministrativeToCollectionIfMissing([], null, uniteAdministrative, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(uniteAdministrative);
      });

      it('should return initial array if no UniteAdministrative is added', () => {
        const uniteAdministrativeCollection: IUniteAdministrative[] = [sampleWithRequiredData];
        expectedResult = service.addUniteAdministrativeToCollectionIfMissing(uniteAdministrativeCollection, undefined, null);
        expect(expectedResult).toEqual(uniteAdministrativeCollection);
      });
    });

    describe('compareUniteAdministrative', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUniteAdministrative(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUniteAdministrative(entity1, entity2);
        const compareResult2 = service.compareUniteAdministrative(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUniteAdministrative(entity1, entity2);
        const compareResult2 = service.compareUniteAdministrative(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUniteAdministrative(entity1, entity2);
        const compareResult2 = service.compareUniteAdministrative(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
