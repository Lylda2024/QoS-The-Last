import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { ISite } from '../site.model';

@Component({
  selector: 'jhi-site-detail',
  templateUrl: './site-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class SiteDetailComponent {
  site = input<ISite | null>(null);

  previousState(): void {
    window.history.back();
  }
}
