import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDegradation } from 'app/entities/degradation/degradation.model';
import { DegradationService } from 'app/entities/degradation/service/degradation.service';
import { HistoriqueService } from '../service/historique.service';
import { IHistorique } from '../historique.model';
import { HistoriqueFormService } from './historique-form.service';

import { HistoriqueUpdateComponent } from './historique-update.component';

describe('Historique Management Update Component', () => {
  let comp: HistoriqueUpdateComponent;
  let fixture: ComponentFixture<HistoriqueUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let historiqueFormService: HistoriqueFormService;
  let historiqueService: HistoriqueService;
  let degradationService: DegradationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HistoriqueUpdateComponent],
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
      .overrideTemplate(HistoriqueUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HistoriqueUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    historiqueFormService = TestBed.inject(HistoriqueFormService);
    historiqueService = TestBed.inject(HistoriqueService);
    degradationService = TestBed.inject(DegradationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Degradation query and add missing value', () => {
      const historique: IHistorique = { id: 11477 };
      const degradation: IDegradation = { id: 14283 };
      historique.degradation = degradation;

      const degradationCollection: IDegradation[] = [{ id: 14283 }];
      jest.spyOn(degradationService, 'query').mockReturnValue(of(new HttpResponse({ body: degradationCollection })));
      const additionalDegradations = [degradation];
      const expectedCollection: IDegradation[] = [...additionalDegradations, ...degradationCollection];
      jest.spyOn(degradationService, 'addDegradationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ historique });
      comp.ngOnInit();

      expect(degradationService.query).toHaveBeenCalled();
      expect(degradationService.addDegradationToCollectionIfMissing).toHaveBeenCalledWith(
        degradationCollection,
        ...additionalDegradations.map(expect.objectContaining),
      );
      expect(comp.degradationsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const historique: IHistorique = { id: 11477 };
      const degradation: IDegradation = { id: 14283 };
      historique.degradation = degradation;

      activatedRoute.data = of({ historique });
      comp.ngOnInit();

      expect(comp.degradationsSharedCollection).toContainEqual(degradation);
      expect(comp.historique).toEqual(historique);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHistorique>>();
      const historique = { id: 5597 };
      jest.spyOn(historiqueFormService, 'getHistorique').mockReturnValue(historique);
      jest.spyOn(historiqueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historique });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: historique }));
      saveSubject.complete();

      // THEN
      expect(historiqueFormService.getHistorique).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(historiqueService.update).toHaveBeenCalledWith(expect.objectContaining(historique));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHistorique>>();
      const historique = { id: 5597 };
      jest.spyOn(historiqueFormService, 'getHistorique').mockReturnValue({ id: null });
      jest.spyOn(historiqueService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historique: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: historique }));
      saveSubject.complete();

      // THEN
      expect(historiqueFormService.getHistorique).toHaveBeenCalled();
      expect(historiqueService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHistorique>>();
      const historique = { id: 5597 };
      jest.spyOn(historiqueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historique });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(historiqueService.update).toHaveBeenCalled();
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
