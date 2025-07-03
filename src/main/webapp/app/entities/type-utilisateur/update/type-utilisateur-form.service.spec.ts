import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../type-utilisateur.test-samples';

import { TypeUtilisateurFormService } from './type-utilisateur-form.service';

describe('TypeUtilisateur Form Service', () => {
  let service: TypeUtilisateurFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeUtilisateurFormService);
  });

  describe('Service methods', () => {
    describe('createTypeUtilisateurFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypeUtilisateurFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            description: expect.any(Object),
            niveau: expect.any(Object),
            permissions: expect.any(Object),
          }),
        );
      });

      it('passing ITypeUtilisateur should create a new form with FormGroup', () => {
        const formGroup = service.createTypeUtilisateurFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            description: expect.any(Object),
            niveau: expect.any(Object),
            permissions: expect.any(Object),
          }),
        );
      });
    });

    describe('getTypeUtilisateur', () => {
      it('should return NewTypeUtilisateur for default TypeUtilisateur initial value', () => {
        const formGroup = service.createTypeUtilisateurFormGroup(sampleWithNewData);

        const typeUtilisateur = service.getTypeUtilisateur(formGroup) as any;

        expect(typeUtilisateur).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypeUtilisateur for empty TypeUtilisateur initial value', () => {
        const formGroup = service.createTypeUtilisateurFormGroup();

        const typeUtilisateur = service.getTypeUtilisateur(formGroup) as any;

        expect(typeUtilisateur).toMatchObject({});
      });

      it('should return ITypeUtilisateur', () => {
        const formGroup = service.createTypeUtilisateurFormGroup(sampleWithRequiredData);

        const typeUtilisateur = service.getTypeUtilisateur(formGroup) as any;

        expect(typeUtilisateur).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypeUtilisateur should not enable id FormControl', () => {
        const formGroup = service.createTypeUtilisateurFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypeUtilisateur should disable id FormControl', () => {
        const formGroup = service.createTypeUtilisateurFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
