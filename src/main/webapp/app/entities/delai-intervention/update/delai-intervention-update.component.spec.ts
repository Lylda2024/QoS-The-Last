import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDegradation } from 'app/entities/degradation/degradation.model';
import { DegradationService } from 'app/entities/degradation/service/degradation.service';
import { DelaiInterventionService } from '../service/DelaiIntervention.service';
import { IDelaiIntervention } from '../DelaiIntervention.model';
import { DelaiInterventionFormService } from './DelaiIntervention-form.service';

import { DelaiInterventionUpdateComponent } from './DelaiIntervention-update.component';

describe('DelaiIntervention Management Update Component', () => {
  let comp: DelaiInterventionUpdateComponent;
  let fixture: ComponentFixture<DelaiInterventionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let DelaiInterventionFormService: DelaiInterventionFormService;
  let DelaiInterventionService: DelaiInterventionService;
  let degradationService: DegradationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DelaiInterventionUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DelaiInterventionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DelaiInterventionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    DelaiInterventionFormService = TestBed.inject(DelaiInterventionFormService);
    DelaiInterventionService = TestBed.inject(DelaiInterventionService);
    degradationService = TestBed.inject(DegradationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Degradation query and add missing value', () => {
      const DelaiIntervention: IDelaiIntervention = { id: 11477 };
      const degradation: IDegradation = { id: 14283 };
      DelaiIntervention.degradation = degradation;

      const degradationCollection: IDegradation[] = [{ id: 14283 }];
      jest.spyOn(degradationService, 'query').mockReturnValue(of(new HttpResponse({ body: degradationCollection })));
      const additionalDegradations = [degradation];
      const expectedCollection: IDegradation[] = [...additionalDegradations, ...degradationCollection];
      jest.spyOn(degradationService, 'addDegradationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ DelaiIntervention });
      comp.ngOnInit();

      expect(degradationService.query).toHaveBeenCalled();
      expect(degradationService.addDegradationToCollectionIfMissing).toHaveBeenCalledWith(
        degradationCollection,
        ...additionalDegradations.map(expect.objectContaining),
      );
      expect(comp.degradationsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const DelaiIntervention: IDelaiIntervention = { id: 11477 };
      const degradation: IDegradation = { id: 14283 };
      DelaiIntervention.degradation = degradation;

      activatedRoute.data = of({ DelaiIntervention });
      comp.ngOnInit();

      expect(comp.degradationsSharedCollection).toContainEqual(degradation);
      expect(comp.DelaiIntervention).toEqual(DelaiIntervention);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDelaiIntervention>>();
      const DelaiIntervention = { id: 5597 };
      jest.spyOn(DelaiInterventionFormService, 'getDelaiIntervention').mockReturnValue(DelaiIntervention);
      jest.spyOn(DelaiInterventionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ DelaiIntervention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: DelaiIntervention }));
      saveSubject.complete();

      // THEN
      expect(DelaiInterventionFormService.getDelaiIntervention).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(DelaiInterventionService.update).toHaveBeenCalledWith(expect.objectContaining(DelaiIntervention));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDelaiIntervention>>();
      const DelaiIntervention = { id: 5597 };
      jest.spyOn(DelaiInterventionFormService, 'getDelaiIntervention').mockReturnValue({ id: null });
      jest.spyOn(DelaiInterventionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ DelaiIntervention: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: DelaiIntervention }));
      saveSubject.complete();

      // THEN
      expect(DelaiInterventionFormService.getDelaiIntervention).toHaveBeenCalled();
      expect(DelaiInterventionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDelaiIntervention>>();
      const DelaiIntervention = { id: 5597 };
      jest.spyOn(DelaiInterventionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ DelaiIntervention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(DelaiInterventionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDegradation', () => {
      it('should forward to degradationService', () => {
        const entity = { id: 14283 };
        const entity2 = { id: 501 };
        jest.spyOn(degradationService, 'compareDegradation');
        comp.compareDegradation(entity, entity2);
        expect(degradationService.compareDegradation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
