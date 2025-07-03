import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDegradation } from '../degradation.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../degradation.test-samples';

import { DegradationService } from './degradation.service';

const requireRestSample: IDegradation = {
  ...sampleWithRequiredData,
};

describe('Degradation Service', () => {
  let service: DegradationService;
  let httpMock: HttpTestingController;
  let expectedResult: IDegradation | IDegradation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DegradationService);
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

    it('should create a Degradation', () => {
      const degradation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(degradation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Degradation', () => {
      const degradation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(degradation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Degradation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Degradation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Degradation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDegradationToCollectionIfMissing', () => {
      it('should add a Degradation to an empty array', () => {
        const degradation: IDegradation = sampleWithRequiredData;
        expectedResult = service.addDegradationToCollectionIfMissing([], degradation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(degradation);
      });

      it('should not add a Degradation to an array that contains it', () => {
        const degradation: IDegradation = sampleWithRequiredData;
        const degradationCollection: IDegradation[] = [
          {
            ...degradation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDegradationToCollectionIfMissing(degradationCollection, degradation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Degradation to an array that doesn't contain it", () => {
        const degradation: IDegradation = sampleWithRequiredData;
        const degradationCollection: IDegradation[] = [sampleWithPartialData];
        expectedResult = service.addDegradationToCollectionIfMissing(degradationCollection, degradation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(degradation);
      });

      it('should add only unique Degradation to an array', () => {
        const degradationArray: IDegradation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const degradationCollection: IDegradation[] = [sampleWithRequiredData];
        expectedResult = service.addDegradationToCollectionIfMissing(degradationCollection, ...degradationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const degradation: IDegradation = sampleWithRequiredData;
        const degradation2: IDegradation = sampleWithPartialData;
        expectedResult = service.addDegradationToCollectionIfMissing([], degradation, degradation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(degradation);
        expect(expectedResult).toContain(degradation2);
      });

      it('should accept null and undefined values', () => {
        const degradation: IDegradation = sampleWithRequiredData;
        expectedResult = service.addDegradationToCollectionIfMissing([], null, degradation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(degradation);
      });

      it('should return initial array if no Degradation is added', () => {
        const degradationCollection: IDegradation[] = [sampleWithRequiredData];
        expectedResult = service.addDegradationToCollectionIfMissing(degradationCollection, undefined, null);
        expect(expectedResult).toEqual(degradationCollection);
      });
    });

    describe('compareDegradation', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDegradation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 14283 };
        const entity2 = null;

        const compareResult1 = service.compareDegradation(entity1, entity2);
        const compareResult2 = service.compareDegradation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 14283 };
        const entity2 = { id: 501 };

        const compareResult1 = service.compareDegradation(entity1, entity2);
        const compareResult2 = service.compareDegradation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 14283 };
        const entity2 = { id: 14283 };

        const compareResult1 = service.compareDegradation(entity1, entity2);
        const compareResult2 = service.compareDegradation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
