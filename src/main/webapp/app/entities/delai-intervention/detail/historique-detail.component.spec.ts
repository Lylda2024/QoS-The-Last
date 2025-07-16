import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DelaiInterventionDetailComponent } from './DelaiIntervention-detail.component';

describe('DelaiIntervention Management Detail Component', () => {
  let comp: DelaiInterventionDetailComponent;
  let fixture: ComponentFixture<DelaiInterventionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DelaiInterventionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./DelaiIntervention-detail.component').then(m => m.DelaiInterventionDetailComponent),
              resolve: { DelaiIntervention: () => of({ id: 5597 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DelaiInterventionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DelaiInterventionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load DelaiIntervention on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DelaiInterventionDetailComponent);

      // THEN
      expect(instance.DelaiIntervention()).toEqual(expect.objectContaining({ id: 5597 }));
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
