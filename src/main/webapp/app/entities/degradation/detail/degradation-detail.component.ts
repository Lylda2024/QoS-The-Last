import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IDegradation } from '../degradation.model';

@Component({
  selector: 'jhi-degradation-detail',
  templateUrl: './degradation-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class DegradationDetailComponent {
  degradation = input<IDegradation | null>(null);

  previousState(): void {
    window.history.back();
  }
}
