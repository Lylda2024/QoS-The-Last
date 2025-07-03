import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITypeUtilisateur } from '../type-utilisateur.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../type-utilisateur.test-samples';

import { TypeUtilisateurService } from './type-utilisateur.service';

const requireRestSample: ITypeUtilisateur = {
  ...sampleWithRequiredData,
};

describe('TypeUtilisateur Service', () => {
  let service: TypeUtilisateurService;
  let httpMock: HttpTestingController;
  let expectedResult: ITypeUtilisateur | ITypeUtilisateur[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TypeUtilisateurService);
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

    it('should create a TypeUtilisateur', () => {
      const typeUtilisateur = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(typeUtilisateur).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeUtilisateur', () => {
      const typeUtilisateur = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(typeUtilisateur).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeUtilisateur', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeUtilisateur', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TypeUtilisateur', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTypeUtilisateurToCollectionIfMissing', () => {
      it('should add a TypeUtilisateur to an empty array', () => {
        const typeUtilisateur: ITypeUtilisateur = sampleWithRequiredData;
        expectedResult = service.addTypeUtilisateurToCollectionIfMissing([], typeUtilisateur);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeUtilisateur);
      });

      it('should not add a TypeUtilisateur to an array that contains it', () => {
        const typeUtilisateur: ITypeUtilisateur = sampleWithRequiredData;
        const typeUtilisateurCollection: ITypeUtilisateur[] = [
          {
            ...typeUtilisateur,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTypeUtilisateurToCollectionIfMissing(typeUtilisateurCollection, typeUtilisateur);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeUtilisateur to an array that doesn't contain it", () => {
        const typeUtilisateur: ITypeUtilisateur = sampleWithRequiredData;
        const typeUtilisateurCollection: ITypeUtilisateur[] = [sampleWithPartialData];
        expectedResult = service.addTypeUtilisateurToCollectionIfMissing(typeUtilisateurCollection, typeUtilisateur);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeUtilisateur);
      });

      it('should add only unique TypeUtilisateur to an array', () => {
        const typeUtilisateurArray: ITypeUtilisateur[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const typeUtilisateurCollection: ITypeUtilisateur[] = [sampleWithRequiredData];
        expectedResult = service.addTypeUtilisateurToCollectionIfMissing(typeUtilisateurCollection, ...typeUtilisateurArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeUtilisateur: ITypeUtilisateur = sampleWithRequiredData;
        const typeUtilisateur2: ITypeUtilisateur = sampleWithPartialData;
        expectedResult = service.addTypeUtilisateurToCollectionIfMissing([], typeUtilisateur, typeUtilisateur2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeUtilisateur);
        expect(expectedResult).toContain(typeUtilisateur2);
      });

      it('should accept null and undefined values', () => {
        const typeUtilisateur: ITypeUtilisateur = sampleWithRequiredData;
        expectedResult = service.addTypeUtilisateurToCollectionIfMissing([], null, typeUtilisateur, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeUtilisateur);
      });

      it('should return initial array if no TypeUtilisateur is added', () => {
        const typeUtilisateurCollection: ITypeUtilisateur[] = [sampleWithRequiredData];
        expectedResult = service.addTypeUtilisateurToCollectionIfMissing(typeUtilisateurCollection, undefined, null);
        expect(expectedResult).toEqual(typeUtilisateurCollection);
      });
    });

    describe('compareTypeUtilisateur', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTypeUtilisateur(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 3387 };
        const entity2 = null;

        const compareResult1 = service.compareTypeUtilisateur(entity1, entity2);
        const compareResult2 = service.compareTypeUtilisateur(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 3387 };
        const entity2 = { id: 13459 };

        const compareResult1 = service.compareTypeUtilisateur(entity1, entity2);
        const compareResult2 = service.compareTypeUtilisateur(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 3387 };
        const entity2 = { id: 3387 };

        const compareResult1 = service.compareTypeUtilisateur(entity1, entity2);
        const compareResult2 = service.compareTypeUtilisateur(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
