import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { TypeUtilisateurService } from '../service/type-utilisateur.service';
import { ITypeUtilisateur } from '../type-utilisateur.model';
import { TypeUtilisateurFormService } from './type-utilisateur-form.service';

import { TypeUtilisateurUpdateComponent } from './type-utilisateur-update.component';

describe('TypeUtilisateur Management Update Component', () => {
  let comp: TypeUtilisateurUpdateComponent;
  let fixture: ComponentFixture<TypeUtilisateurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeUtilisateurFormService: TypeUtilisateurFormService;
  let typeUtilisateurService: TypeUtilisateurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TypeUtilisateurUpdateComponent],
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
      .overrideTemplate(TypeUtilisateurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeUtilisateurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeUtilisateurFormService = TestBed.inject(TypeUtilisateurFormService);
    typeUtilisateurService = TestBed.inject(TypeUtilisateurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const typeUtilisateur: ITypeUtilisateur = { id: 13459 };

      activatedRoute.data = of({ typeUtilisateur });
      comp.ngOnInit();

      expect(comp.typeUtilisateur).toEqual(typeUtilisateur);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeUtilisateur>>();
      const typeUtilisateur = { id: 3387 };
      jest.spyOn(typeUtilisateurFormService, 'getTypeUtilisateur').mockReturnValue(typeUtilisateur);
      jest.spyOn(typeUtilisateurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeUtilisateur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeUtilisateur }));
      saveSubject.complete();

      // THEN
      expect(typeUtilisateurFormService.getTypeUtilisateur).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeUtilisateurService.update).toHaveBeenCalledWith(expect.objectContaining(typeUtilisateur));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeUtilisateur>>();
      const typeUtilisateur = { id: 3387 };
      jest.spyOn(typeUtilisateurFormService, 'getTypeUtilisateur').mockReturnValue({ id: null });
      jest.spyOn(typeUtilisateurService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeUtilisateur: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeUtilisateur }));
      saveSubject.complete();

      // THEN
      expect(typeUtilisateurFormService.getTypeUtilisateur).toHaveBeenCalled();
      expect(typeUtilisateurService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeUtilisateur>>();
      const typeUtilisateur = { id: 3387 };
      jest.spyOn(typeUtilisateurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeUtilisateur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeUtilisateurService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
