import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IHistorique } from '../historique.model';
import { HistoriqueService } from '../service/historique.service';

@Component({
  templateUrl: './historique-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class HistoriqueDeleteDialogComponent {
  historique?: IHistorique;

  protected historiqueService = inject(HistoriqueService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.historiqueService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
