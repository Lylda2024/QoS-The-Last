import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DegradationDetailComponent } from './degradation-detail.component';

describe('Degradation Management Detail Component', () => {
  let comp: DegradationDetailComponent;
  let fixture: ComponentFixture<DegradationDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DegradationDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./degradation-detail.component').then(m => m.DegradationDetailComponent),
              resolve: { degradation: () => of({ id: 14283 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DegradationDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DegradationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load degradation on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DegradationDetailComponent);

      // THEN
      expect(instance.degradation()).toEqual(expect.objectContaining({ id: 14283 }));
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
