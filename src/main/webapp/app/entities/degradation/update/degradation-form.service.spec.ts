import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../degradation.test-samples';

import { DegradationFormService } from './degradation-form.service';

describe('Degradation Form Service', () => {
  let service: DegradationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DegradationFormService);
  });

  describe('Service methods', () => {
    describe('createDegradationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDegradationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numero: expect.any(Object),
            localite: expect.any(Object),
            contactTemoin: expect.any(Object),
            typeAnomalie: expect.any(Object),
            priorite: expect.any(Object),
            problem: expect.any(Object),
            porteur: expect.any(Object),
            actionsEffectuees: expect.any(Object),
            utilisateur: expect.any(Object),
            site: expect.any(Object),
          }),
        );
      });

      it('passing IDegradation should create a new form with FormGroup', () => {
        const formGroup = service.createDegradationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numero: expect.any(Object),
            localite: expect.any(Object),
            contactTemoin: expect.any(Object),
            typeAnomalie: expect.any(Object),
            priorite: expect.any(Object),
            problem: expect.any(Object),
            porteur: expect.any(Object),
            actionsEffectuees: expect.any(Object),
            utilisateur: expect.any(Object),
            site: expect.any(Object),
          }),
        );
      });
    });

    describe('getDegradation', () => {
      it('should return NewDegradation for default Degradation initial value', () => {
        const formGroup = service.createDegradationFormGroup(sampleWithNewData);

        const degradation = service.getDegradation(formGroup) as any;

        expect(degradation).toMatchObject(sampleWithNewData);
      });

      it('should return NewDegradation for empty Degradation initial value', () => {
        const formGroup = service.createDegradationFormGroup();

        const degradation = service.getDegradation(formGroup) as any;

        expect(degradation).toMatchObject({});
      });

      it('should return IDegradation', () => {
        const formGroup = service.createDegradationFormGroup(sampleWithRequiredData);

        const degradation = service.getDegradation(formGroup) as any;

        expect(degradation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDegradation should not enable id FormControl', () => {
        const formGroup = service.createDegradationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDegradation should disable id FormControl', () => {
        const formGroup = service.createDegradationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
