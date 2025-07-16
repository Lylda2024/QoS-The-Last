import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../DelaiIntervention.test-samples';

import { DelaiInterventionFormService } from './DelaiIntervention-form.service';

describe('DelaiIntervention Form Service', () => {
  let service: DelaiInterventionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DelaiInterventionFormService);
  });

  describe('Service methods', () => {
    describe('createDelaiInterventionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDelaiInterventionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            utilisateur: expect.any(Object),
            section: expect.any(Object),
            horodatage: expect.any(Object),
            degradation: expect.any(Object),
          }),
        );
      });

      it('passing IDelaiIntervention should create a new form with FormGroup', () => {
        const formGroup = service.createDelaiInterventionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            utilisateur: expect.any(Object),
            section: expect.any(Object),
            horodatage: expect.any(Object),
            degradation: expect.any(Object),
          }),
        );
      });
    });

    describe('getDelaiIntervention', () => {
      it('should return NewDelaiIntervention for default DelaiIntervention initial value', () => {
        const formGroup = service.createDelaiInterventionFormGroup(sampleWithNewData);

        const DelaiIntervention = service.getDelaiIntervention(formGroup) as any;

        expect(DelaiIntervention).toMatchObject(sampleWithNewData);
      });

      it('should return NewDelaiIntervention for empty DelaiIntervention initial value', () => {
        const formGroup = service.createDelaiInterventionFormGroup();

        const DelaiIntervention = service.getDelaiIntervention(formGroup) as any;

        expect(DelaiIntervention).toMatchObject({});
      });

      it('should return IDelaiIntervention', () => {
        const formGroup = service.createDelaiInterventionFormGroup(sampleWithRequiredData);

        const DelaiIntervention = service.getDelaiIntervention(formGroup) as any;

        expect(DelaiIntervention).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDelaiIntervention should not enable id FormControl', () => {
        const formGroup = service.createDelaiInterventionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDelaiIntervention should disable id FormControl', () => {
        const formGroup = service.createDelaiInterventionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
