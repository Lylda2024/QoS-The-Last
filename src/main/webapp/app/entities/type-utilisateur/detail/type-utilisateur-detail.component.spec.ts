import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TypeUtilisateurDetailComponent } from './type-utilisateur-detail.component';

describe('TypeUtilisateur Management Detail Component', () => {
  let comp: TypeUtilisateurDetailComponent;
  let fixture: ComponentFixture<TypeUtilisateurDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TypeUtilisateurDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./type-utilisateur-detail.component').then(m => m.TypeUtilisateurDetailComponent),
              resolve: { typeUtilisateur: () => of({ id: 3387 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TypeUtilisateurDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TypeUtilisateurDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load typeUtilisateur on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TypeUtilisateurDetailComponent);

      // THEN
      expect(instance.typeUtilisateur()).toEqual(expect.objectContaining({ id: 3387 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
