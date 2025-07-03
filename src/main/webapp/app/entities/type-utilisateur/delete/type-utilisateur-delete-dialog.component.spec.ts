jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { TypeUtilisateurService } from '../service/type-utilisateur.service';

import { TypeUtilisateurDeleteDialogComponent } from './type-utilisateur-delete-dialog.component';

describe('TypeUtilisateur Management Delete Component', () => {
  let comp: TypeUtilisateurDeleteDialogComponent;
  let fixture: ComponentFixture<TypeUtilisateurDeleteDialogComponent>;
  let service: TypeUtilisateurService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TypeUtilisateurDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(TypeUtilisateurDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TypeUtilisateurDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TypeUtilisateurService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
