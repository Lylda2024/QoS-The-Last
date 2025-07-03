import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITypeUtilisateur } from '../type-utilisateur.model';
import { TypeUtilisateurService } from '../service/type-utilisateur.service';

@Component({
  templateUrl: './type-utilisateur-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TypeUtilisateurDeleteDialogComponent {
  typeUtilisateur?: ITypeUtilisateur;

  protected typeUtilisateurService = inject(TypeUtilisateurService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeUtilisateurService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
