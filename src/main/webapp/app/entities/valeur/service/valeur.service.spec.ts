import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IValeur } from '../valeur.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../valeur.test-samples';

import { ValeurService } from './valeur.service';

const requireRestSample: IValeur = {
  ...sampleWithRequiredData,
};

describe('Valeur Service', () => {
  let service: ValeurService;
  let httpMock: HttpTestingController;
  let expectedResult: IValeur | IValeur[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ValeurService);
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

    it('should create a Valeur', () => {
      const valeur = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(valeur).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Valeur', () => {
      const valeur = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(valeur).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Valeur', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Valeur', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Valeur', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Valeur', () => {
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

    describe('addValeurToCollectionIfMissing', () => {
      it('should add a Valeur to an empty array', () => {
        const valeur: IValeur = sampleWithRequiredData;
        expectedResult = service.addValeurToCollectionIfMissing([], valeur);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(valeur);
      });

      it('should not add a Valeur to an array that contains it', () => {
        const valeur: IValeur = sampleWithRequiredData;
        const valeurCollection: IValeur[] = [
          {
            ...valeur,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addValeurToCollectionIfMissing(valeurCollection, valeur);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Valeur to an array that doesn't contain it", () => {
        const valeur: IValeur = sampleWithRequiredData;
        const valeurCollection: IValeur[] = [sampleWithPartialData];
        expectedResult = service.addValeurToCollectionIfMissing(valeurCollection, valeur);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(valeur);
      });

      it('should add only unique Valeur to an array', () => {
        const valeurArray: IValeur[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const valeurCollection: IValeur[] = [sampleWithRequiredData];
        expectedResult = service.addValeurToCollectionIfMissing(valeurCollection, ...valeurArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const valeur: IValeur = sampleWithRequiredData;
        const valeur2: IValeur = sampleWithPartialData;
        expectedResult = service.addValeurToCollectionIfMissing([], valeur, valeur2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(valeur);
        expect(expectedResult).toContain(valeur2);
      });

      it('should accept null and undefined values', () => {
        const valeur: IValeur = sampleWithRequiredData;
        expectedResult = service.addValeurToCollectionIfMissing([], null, valeur, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(valeur);
      });

      it('should return initial array if no Valeur is added', () => {
        const valeurCollection: IValeur[] = [sampleWithRequiredData];
        expectedResult = service.addValeurToCollectionIfMissing(valeurCollection, undefined, null);
        expect(expectedResult).toEqual(valeurCollection);
      });
    });

    describe('compareValeur', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareValeur(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareValeur(entity1, entity2);
        const compareResult2 = service.compareValeur(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareValeur(entity1, entity2);
        const compareResult2 = service.compareValeur(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareValeur(entity1, entity2);
        const compareResult2 = service.compareValeur(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
