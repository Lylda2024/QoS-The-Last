import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDelaiIntervention } from '../DelaiIntervention.model';
import { DelaiInterventionService } from '../service/DelaiIntervention.service';

@Component({
  templateUrl: './DelaiIntervention-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DelaiInterventionDeleteDialogComponent {
  DelaiIntervention?: IDelaiIntervention;

  protected DelaiInterventionService = inject(DelaiInterventionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.DelaiInterventionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
