import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDelaiIntervention } from '../delai-intervention.model';
import { DelaiInterventionService } from '../service/delai-intervention.service';

@Component({
  templateUrl: './delai-intervention-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DelaiInterventionDeleteDialogComponent {
  delaiIntervention?: IDelaiIntervention;

  protected delaiInterventionService = inject(DelaiInterventionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.delaiInterventionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
