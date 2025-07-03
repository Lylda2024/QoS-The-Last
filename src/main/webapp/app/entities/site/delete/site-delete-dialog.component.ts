import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISite } from '../site.model';
import { SiteService } from '../service/site.service';

@Component({
  templateUrl: './site-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SiteDeleteDialogComponent {
  site?: ISite;

  protected siteService = inject(SiteService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.siteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
