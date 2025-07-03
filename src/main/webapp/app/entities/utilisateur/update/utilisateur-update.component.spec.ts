import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ITypeUtilisateur } from 'app/entities/type-utilisateur/type-utilisateur.model';
import { TypeUtilisateurService } from 'app/entities/type-utilisateur/service/type-utilisateur.service';
import { IRole } from 'app/entities/role/role.model';
import { RoleService } from 'app/entities/role/service/role.service';
import { IUtilisateur } from '../utilisateur.model';
import { UtilisateurService } from '../service/utilisateur.service';
import { UtilisateurFormService } from './utilisateur-form.service';

import { UtilisateurUpdateComponent } from './utilisateur-update.component';

describe('Utilisateur Management Update Component', () => {
  let comp: UtilisateurUpdateComponent;
  let fixture: ComponentFixture<UtilisateurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let utilisateurFormService: UtilisateurFormService;
  let utilisateurService: UtilisateurService;
  let typeUtilisateurService: TypeUtilisateurService;
  let roleService: RoleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [UtilisateurUpdateComponent],
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
      .overrideTemplate(UtilisateurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UtilisateurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    utilisateurFormService = TestBed.inject(UtilisateurFormService);
    utilisateurService = TestBed.inject(UtilisateurService);
    typeUtilisateurService = TestBed.inject(TypeUtilisateurService);
    roleService = TestBed.inject(RoleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call TypeUtilisateur query and add missing value', () => {
      const utilisateur: IUtilisateur = { id: 31928 };
      const typeUtilisateur: ITypeUtilisateur = { id: 3387 };
      utilisateur.typeUtilisateur = typeUtilisateur;

      const typeUtilisateurCollection: ITypeUtilisateur[] = [{ id: 3387 }];
      jest.spyOn(typeUtilisateurService, 'query').mockReturnValue(of(new HttpResponse({ body: typeUtilisateurCollection })));
      const additionalTypeUtilisateurs = [typeUtilisateur];
      const expectedCollection: ITypeUtilisateur[] = [...additionalTypeUtilisateurs, ...typeUtilisateurCollection];
      jest.spyOn(typeUtilisateurService, 'addTypeUtilisateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ utilisateur });
      comp.ngOnInit();

      expect(typeUtilisateurService.query).toHaveBeenCalled();
      expect(typeUtilisateurService.addTypeUtilisateurToCollectionIfMissing).toHaveBeenCalledWith(
        typeUtilisateurCollection,
        ...additionalTypeUtilisateurs.map(expect.objectContaining),
      );
      expect(comp.typeUtilisateursSharedCollection).toEqual(expectedCollection);
    });

    it('should call Role query and add missing value', () => {
      const utilisateur: IUtilisateur = { id: 31928 };
      const roles: IRole[] = [{ id: 12873 }];
      utilisateur.roles = roles;

      const roleCollection: IRole[] = [{ id: 12873 }];
      jest.spyOn(roleService, 'query').mockReturnValue(of(new HttpResponse({ body: roleCollection })));
      const additionalRoles = [...roles];
      const expectedCollection: IRole[] = [...additionalRoles, ...roleCollection];
      jest.spyOn(roleService, 'addRoleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ utilisateur });
      comp.ngOnInit();

      expect(roleService.query).toHaveBeenCalled();
      expect(roleService.addRoleToCollectionIfMissing).toHaveBeenCalledWith(
        roleCollection,
        ...additionalRoles.map(expect.objectContaining),
      );
      expect(comp.rolesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const utilisateur: IUtilisateur = { id: 31928 };
      const typeUtilisateur: ITypeUtilisateur = { id: 3387 };
      utilisateur.typeUtilisateur = typeUtilisateur;
      const role: IRole = { id: 12873 };
      utilisateur.roles = [role];

      activatedRoute.data = of({ utilisateur });
      comp.ngOnInit();

      expect(comp.typeUtilisateursSharedCollection).toContainEqual(typeUtilisateur);
      expect(comp.rolesSharedCollection).toContainEqual(role);
      expect(comp.utilisateur).toEqual(utilisateur);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUtilisateur>>();
      const utilisateur = { id: 2179 };
      jest.spyOn(utilisateurFormService, 'getUtilisateur').mockReturnValue(utilisateur);
      jest.spyOn(utilisateurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utilisateur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: utilisateur }));
      saveSubject.complete();

      // THEN
      expect(utilisateurFormService.getUtilisateur).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(utilisateurService.update).toHaveBeenCalledWith(expect.objectContaining(utilisateur));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUtilisateur>>();
      const utilisateur = { id: 2179 };
      jest.spyOn(utilisateurFormService, 'getUtilisateur').mockReturnValue({ id: null });
      jest.spyOn(utilisateurService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utilisateur: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: utilisateur }));
      saveSubject.complete();

      // THEN
      expect(utilisateurFormService.getUtilisateur).toHaveBeenCalled();
      expect(utilisateurService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUtilisateur>>();
      const utilisateur = { id: 2179 };
      jest.spyOn(utilisateurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utilisateur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(utilisateurService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTypeUtilisateur', () => {
      it('should forward to typeUtilisateurService', () => {
        const entity = { id: 3387 };
        const entity2 = { id: 13459 };
        jest.spyOn(typeUtilisateurService, 'compareTypeUtilisateur');
        comp.compareTypeUtilisateur(entity, entity2);
        expect(typeUtilisateurService.compareTypeUtilisateur).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRole', () => {
      it('should forward to roleService', () => {
        const entity = { id: 12873 };
        const entity2 = { id: 333 };
        jest.spyOn(roleService, 'compareRole');
        comp.compareRole(entity, entity2);
        expect(roleService.compareRole).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
