import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AlertErrorComponent } from 'app/shared/alert/alert-error.component';
import { AlertComponent } from 'app/shared/alert/alert.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import dayjs from 'dayjs';

import { IDegradation } from '../degradation.model';
import { IDelaiIntervention } from 'app/entities/delai-intervention/delai-intervention.model';
import { DelaiInterventionService } from 'app/entities/delai-intervention/service/delai-intervention.service';

@Component({
  selector: 'jhi-degradation-detail',
  standalone: true,
  templateUrl: './degradation-detail.component.html',
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
      error: err => console.error('Erreur chargement délais', err),
    });
  }

  previousState(): void {
    window.history.back();
  }

  // Calcul de la durée en jours entre dateDebut et dateLimite pour un délai
  getDuree(delai: IDelaiIntervention): number | null {
    if (delai.dateDebut && delai.dateLimite) {
      const debut = dayjs(delai.dateDebut);
      const limite = dayjs(delai.dateLimite);
      if (debut.isValid() && limite.isValid()) {
        return limite.diff(debut, 'day');
      }
    }
    return null;
  }

  // Calcul de la date limite d’intervention à afficher
  getDateLimiteIntervention(): string | null {
    if (!this.degradation?.dateDetection || !this.degradation?.priorite) {
      return null;
    }

    const dateBase = new Date(this.degradation.dateDetection);

    // Si un délai existe, on prend le premier délai et calcule la date limite via sa durée
    if (this.delais.length > 0) {
      const duree = this.getDuree(this.delais[0]);
      if (duree !== null) {
        const dateLimite = new Date(dateBase);
        dateLimite.setDate(dateLimite.getDate() + duree);
        return dateLimite.toLocaleDateString();
      }
    }

    // Sinon, durée par défaut selon priorité
    let joursParPriorite = 0;
    switch (this.degradation.priorite) {
      case 'ELEVEE':
        joursParPriorite = 5;
        break;
      case 'MOYENNE':
        joursParPriorite = 10;
        break;
      case 'FAIBLE':
        joursParPriorite = 20;
        break;
      default:
        joursParPriorite = 7;
    }

    const dateLimite = new Date(dateBase);
    dateLimite.setDate(dateLimite.getDate() + joursParPriorite);
    return dateLimite.toLocaleDateString();
  }
}
