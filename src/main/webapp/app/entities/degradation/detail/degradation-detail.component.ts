import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import dayjs from 'dayjs';

import { IDegradation } from '../degradation.model';
import { IDelaiIntervention } from 'app/entities/delai-intervention/delai-intervention.model';
import { DelaiInterventionService } from 'app/entities/delai-intervention/service/delai-intervention.service';

@Component({
  selector: 'jhi-degradation-detail',
  standalone: true,
  templateUrl: './degradation-detail.component.html',
  imports: [CommonModule, RouterModule, FontAwesomeModule],
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

  /** Durée en jours entre dateDebut et dateLimite d’un délai */
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

  /** Date limite d’intervention (P1 / P2 / P3 uniquement) */
  getDateLimiteIntervention(): string | null {
    if (!this.degradation?.dateDetection || !this.degradation?.priorite) {
      return null;
    }

    // Convertir dateDetection en dayjs
    const dateDetectionDayjs = dayjs(this.degradation.dateDetection);
    if (!dateDetectionDayjs.isValid()) {
      return null;
    }

    const dateBase = dateDetectionDayjs.toDate();

    // 1) Si un délai existe → on l’utilise
    if (this.delais.length > 0) {
      const duree = this.getDuree(this.delais[0]);
      if (duree !== null) {
        const dateLimite = new Date(dateBase);
        dateLimite.setDate(dateLimite.getDate() + duree);
        return dateLimite.toLocaleDateString();
      }
    }

    // 2) Sinon, règles P1 / P2 / P3 uniquement
    let jours = 0;
    switch (this.degradation.priorite) {
      case 'P1':
        jours = 5;
        break;
      case 'P2':
        jours = 10;
        break;
      case 'P3':
        jours = 20;
        break;
      default:
        return null;
    }

    const dateLimite = new Date(dateBase);
    dateLimite.setDate(dateLimite.getDate() + jours);
    return dateLimite.toLocaleDateString();
  }

  /** Getter corrigé pour dateDetection */
  get dateDetectionDate(): Date | null {
    if (!this.degradation?.dateDetection) return null;
    return dayjs(this.degradation.dateDetection).toDate();
  }

  /** Getter générique pour convertir n'importe quelle date en objet Date */
  getDate(date: any): Date | null {
    return date ? dayjs(date).toDate() : null;
  }

  prolongerDelai(delai: IDelaiIntervention): void {
    console.log('Prolonger délai', delai);
  }

  transfererDelai(delai: IDelaiIntervention): void {
    console.log('Transférer délai', delai);
  }
}
