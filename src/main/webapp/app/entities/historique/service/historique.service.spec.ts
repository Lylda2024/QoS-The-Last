import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IHistorique } from '../historique.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../historique.test-samples';

import { HistoriqueService, RestHistorique } from './historique.service';

const requireRestSample: RestHistorique = {
  ...sampleWithRequiredData,
  horodatage: sampleWithRequiredData.horodatage?.toJSON(),
};

describe('Historique Service', () => {
  let service: HistoriqueService;
  let httpMock: HttpTestingController;
  let expectedResult: IHistorique | IHistorique[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(HistoriqueService);
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

    it('should create a Historique', () => {
      const historique = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(historique).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Historique', () => {
      const historique = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(historique).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Historique', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Historique', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Historique', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addHistoriqueToCollectionIfMissing', () => {
      it('should add a Historique to an empty array', () => {
        const historique: IHistorique = sampleWithRequiredData;
        expectedResult = service.addHistoriqueToCollectionIfMissing([], historique);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(historique);
      });

      it('should not add a Historique to an array that contains it', () => {
        const historique: IHistorique = sampleWithRequiredData;
        const historiqueCollection: IHistorique[] = [
          {
            ...historique,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHistoriqueToCollectionIfMissing(historiqueCollection, historique);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Historique to an array that doesn't contain it", () => {
        const historique: IHistorique = sampleWithRequiredData;
        const historiqueCollection: IHistorique[] = [sampleWithPartialData];
        expectedResult = service.addHistoriqueToCollectionIfMissing(historiqueCollection, historique);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(historique);
      });

      it('should add only unique Historique to an array', () => {
        const historiqueArray: IHistorique[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const historiqueCollection: IHistorique[] = [sampleWithRequiredData];
        expectedResult = service.addHistoriqueToCollectionIfMissing(historiqueCollection, ...historiqueArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const historique: IHistorique = sampleWithRequiredData;
        const historique2: IHistorique = sampleWithPartialData;
        expectedResult = service.addHistoriqueToCollectionIfMissing([], historique, historique2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(historique);
        expect(expectedResult).toContain(historique2);
      });

      it('should accept null and undefined values', () => {
        const historique: IHistorique = sampleWithRequiredData;
        expectedResult = service.addHistoriqueToCollectionIfMissing([], null, historique, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(historique);
      });

      it('should return initial array if no Historique is added', () => {
        const historiqueCollection: IHistorique[] = [sampleWithRequiredData];
        expectedResult = service.addHistoriqueToCollectionIfMissing(historiqueCollection, undefined, null);
        expect(expectedResult).toEqual(historiqueCollection);
      });
    });

    describe('compareHistorique', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHistorique(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5597 };
        const entity2 = null;

        const compareResult1 = service.compareHistorique(entity1, entity2);
        const compareResult2 = service.compareHistorique(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5597 };
        const entity2 = { id: 11477 };

        const compareResult1 = service.compareHistorique(entity1, entity2);
        const compareResult2 = service.compareHistorique(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 5597 };
        const entity2 = { id: 5597 };

        const compareResult1 = service.compareHistorique(entity1, entity2);
        const compareResult2 = service.compareHistorique(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
