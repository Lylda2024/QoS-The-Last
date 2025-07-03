import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDegradation } from 'app/entities/degradation/degradation.model';
import { DegradationService } from 'app/entities/degradation/service/degradation.service';
import { NotificationService } from '../service/notification.service';
import { INotification } from '../notification.model';
import { NotificationFormService } from './notification-form.service';

import { NotificationUpdateComponent } from './notification-update.component';

describe('Notification Management Update Component', () => {
  let comp: NotificationUpdateComponent;
  let fixture: ComponentFixture<NotificationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let notificationFormService: NotificationFormService;
  let notificationService: NotificationService;
  let degradationService: DegradationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [NotificationUpdateComponent],
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
      .overrideTemplate(NotificationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NotificationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    notificationFormService = TestBed.inject(NotificationFormService);
    notificationService = TestBed.inject(NotificationService);
    degradationService = TestBed.inject(DegradationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Degradation query and add missing value', () => {
      const notification: INotification = { id: 16244 };
      const degradation: IDegradation = { id: 14283 };
      notification.degradation = degradation;

      const degradationCollection: IDegradation[] = [{ id: 14283 }];
      jest.spyOn(degradationService, 'query').mockReturnValue(of(new HttpResponse({ body: degradationCollection })));
      const additionalDegradations = [degradation];
      const expectedCollection: IDegradation[] = [...additionalDegradations, ...degradationCollection];
      jest.spyOn(degradationService, 'addDegradationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ notification });
      comp.ngOnInit();

      expect(degradationService.query).toHaveBeenCalled();
      expect(degradationService.addDegradationToCollectionIfMissing).toHaveBeenCalledWith(
        degradationCollection,
        ...additionalDegradations.map(expect.objectContaining),
      );
      expect(comp.degradationsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const notification: INotification = { id: 16244 };
      const degradation: IDegradation = { id: 14283 };
      notification.degradation = degradation;

      activatedRoute.data = of({ notification });
      comp.ngOnInit();

      expect(comp.degradationsSharedCollection).toContainEqual(degradation);
      expect(comp.notification).toEqual(notification);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotification>>();
      const notification = { id: 16124 };
      jest.spyOn(notificationFormService, 'getNotification').mockReturnValue(notification);
      jest.spyOn(notificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notification }));
      saveSubject.complete();

      // THEN
      expect(notificationFormService.getNotification).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(notificationService.update).toHaveBeenCalledWith(expect.objectContaining(notification));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotification>>();
      const notification = { id: 16124 };
      jest.spyOn(notificationFormService, 'getNotification').mockReturnValue({ id: null });
      jest.spyOn(notificationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notification: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notification }));
      saveSubject.complete();

      // THEN
      expect(notificationFormService.getNotification).toHaveBeenCalled();
      expect(notificationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotification>>();
      const notification = { id: 16124 };
      jest.spyOn(notificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(notificationService.update).toHaveBeenCalled();
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
