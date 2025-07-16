import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDelaiIntervention } from '../DelaiIntervention.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../DelaiIntervention.test-samples';

import { DelaiInterventionService, RestDelaiIntervention } from './DelaiIntervention.service';

const requireRestSample: RestDelaiIntervention = {
  ...sampleWithRequiredData,
  horodatage: sampleWithRequiredData.horodatage?.toJSON(),
};

describe('DelaiIntervention Service', () => {
  let service: DelaiInterventionService;
  let httpMock: HttpTestingController;
  let expectedResult: IDelaiIntervention | IDelaiIntervention[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DelaiInterventionService);
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

    it('should create a DelaiIntervention', () => {
      const DelaiIntervention = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(DelaiIntervention).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DelaiIntervention', () => {
      const DelaiIntervention = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(DelaiIntervention).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DelaiIntervention', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DelaiIntervention', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DelaiIntervention', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDelaiInterventionToCollectionIfMissing', () => {
      it('should add a DelaiIntervention to an empty array', () => {
        const DelaiIntervention: IDelaiIntervention = sampleWithRequiredData;
        expectedResult = service.addDelaiInterventionToCollectionIfMissing([], DelaiIntervention);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(DelaiIntervention);
      });

      it('should not add a DelaiIntervention to an array that contains it', () => {
        const DelaiIntervention: IDelaiIntervention = sampleWithRequiredData;
        const DelaiInterventionCollection: IDelaiIntervention[] = [
          {
            ...DelaiIntervention,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDelaiInterventionToCollectionIfMissing(DelaiInterventionCollection, DelaiIntervention);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DelaiIntervention to an array that doesn't contain it", () => {
        const DelaiIntervention: IDelaiIntervention = sampleWithRequiredData;
        const DelaiInterventionCollection: IDelaiIntervention[] = [sampleWithPartialData];
        expectedResult = service.addDelaiInterventionToCollectionIfMissing(DelaiInterventionCollection, DelaiIntervention);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(DelaiIntervention);
      });

      it('should add only unique DelaiIntervention to an array', () => {
        const DelaiInterventionArray: IDelaiIntervention[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const DelaiInterventionCollection: IDelaiIntervention[] = [sampleWithRequiredData];
        expectedResult = service.addDelaiInterventionToCollectionIfMissing(DelaiInterventionCollection, ...DelaiInterventionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const DelaiIntervention: IDelaiIntervention = sampleWithRequiredData;
        const DelaiIntervention2: IDelaiIntervention = sampleWithPartialData;
        expectedResult = service.addDelaiInterventionToCollectionIfMissing([], DelaiIntervention, DelaiIntervention2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(DelaiIntervention);
        expect(expectedResult).toContain(DelaiIntervention2);
      });

      it('should accept null and undefined values', () => {
        const DelaiIntervention: IDelaiIntervention = sampleWithRequiredData;
        expectedResult = service.addDelaiInterventionToCollectionIfMissing([], null, DelaiIntervention, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(DelaiIntervention);
      });

      it('should return initial array if no DelaiIntervention is added', () => {
        const DelaiInterventionCollection: IDelaiIntervention[] = [sampleWithRequiredData];
        expectedResult = service.addDelaiInterventionToCollectionIfMissing(DelaiInterventionCollection, undefined, null);
        expect(expectedResult).toEqual(DelaiInterventionCollection);
      });
    });

    describe('compareDelaiIntervention', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDelaiIntervention(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5597 };
        const entity2 = null;

        const compareResult1 = service.compareDelaiIntervention(entity1, entity2);
        const compareResult2 = service.compareDelaiIntervention(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5597 };
        const entity2 = { id: 11477 };

        const compareResult1 = service.compareDelaiIntervention(entity1, entity2);
        const compareResult2 = service.compareDelaiIntervention(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 5597 };
        const entity2 = { id: 5597 };

        const compareResult1 = service.compareDelaiIntervention(entity1, entity2);
        const compareResult2 = service.compareDelaiIntervention(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
