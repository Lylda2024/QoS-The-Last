import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IHistorique } from '../historique.model';

@Component({
  selector: 'jhi-historique-detail',
  templateUrl: './historique-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class HistoriqueDetailComponent {
  historique = input<IHistorique | null>(null);

  previousState(): void {
    window.history.back();
  }
}
