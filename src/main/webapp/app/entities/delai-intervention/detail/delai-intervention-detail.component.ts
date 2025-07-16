import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';

import { IDelaiIntervention } from '../../delai-intervention/delai-intervention.model';
import { Dayjs } from 'dayjs';

@Component({
  selector: 'jhi-delai-intervention-detail',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule, RouterModule],
  templateUrl: './delai-intervention-detail.component.html',
})
export class DelaiInterventionDetailComponent implements OnInit {
  delaiIntervention?: IDelaiIntervention;

  constructor(
    private route: ActivatedRoute,
    private iconLibrary: FaIconLibrary,
  ) {
    this.iconLibrary.addIcons(faArrowLeft, faPencilAlt);
  }

  ngOnInit(): void {
    this.route.data.subscribe(({ delaiIntervention }) => {
      this.delaiIntervention = delaiIntervention;
    });
  }

  previousState(): void {
    window.history.back();
  }

  /**
   * Convertit un objet Dayjs (ou undefined) en Date ou null
   */
  toDate(value?: Dayjs): Date | null {
    return value ? value.toDate() : null;
  }
}
