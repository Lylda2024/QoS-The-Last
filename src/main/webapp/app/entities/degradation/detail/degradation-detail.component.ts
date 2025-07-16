import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AlertErrorComponent } from 'app/shared/alert/alert-error.component';
import { AlertComponent } from 'app/shared/alert/alert.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { IDegradation } from '../degradation.model';
import { IDelaiIntervention } from 'app/entities/delai-intervention/delai-intervention.model'; // ✅ chemin corrigé
import { DelaiInterventionService } from 'app/entities/delai-intervention/service/delai-intervention.service'; // ✅ chemin corrigé

@Component({
  selector: 'jhi-degradation-detail',
  standalone: true,
  templateUrl: './degradation-detail.component.html', // ✅ assure-toi que ce fichier existe
  imports: [CommonModule, RouterModule, AlertErrorComponent, AlertComponent, FontAwesomeModule],
})
export class DegradationDetailComponent implements OnChanges {
  @Input() degradation: IDegradation | null = null;

  delais: IDelaiIntervention[] = [];

  constructor(private delaiService: DelaiInterventionService) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['degradation'] && this.degradation?.id) {
      this.loadDelais(this.degradation.id);
    }
  }

  loadDelais(degradationId: number): void {
    this.delaiService.getDelaisByDegradationId(degradationId).subscribe({
      next: (data: IDelaiIntervention[]) => {
        this.delais = data;
      },
      error: (err: any) => console.error('Erreur chargement délais', err),
    });
  }

  previousState(): void {
    window.history.back();
  }
}
