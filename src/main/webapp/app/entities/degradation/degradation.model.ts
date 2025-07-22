import dayjs, { Dayjs } from 'dayjs';
import { ISite } from 'app/entities/site/site.model';
import { IDelaiIntervention } from 'app/entities/delai-intervention/delai-intervention.model';
import { FormControl, FormGroup } from '@angular/forms';

export interface IDegradation {
  id: number;
  localite?: string | null;
  contactTemoin?: string | null;
  typeAnomalie?: string | null;
  priorite?: string | null;
  porteur?: string | null;
  porteur2?: string | null;
  statut?: string | null;
  actionsEffectuees?: string | null;
  dateDetection?: Dayjs | null;
  dateLimite?: Dayjs | null;
  commentaire?: string | null;
  nextStep?: string | null;
  ticketOceane?: string | null;
  site?: ISite | null;
  delais?: IDelaiIntervention[] | null;
}

export type NewDegradation = Omit<IDegradation, 'id'> & { id: null };

export type DegradationFormGroupInput = IDegradation | Partial<NewDegradation>;

export type DegradationFormGroupContent = {
  id: FormControl<IDegradation['id'] | null>;
  localite: FormControl<IDegradation['localite'] | null>;
  contactTemoin: FormControl<IDegradation['contactTemoin'] | null>;
  typeAnomalie: FormControl<IDegradation['typeAnomalie'] | null>;
  priorite: FormControl<IDegradation['priorite'] | null>;
  porteur: FormControl<IDegradation['porteur'] | null>;
  porteur2: FormControl<IDegradation['porteur2'] | null>;
  statut: FormControl<IDegradation['statut'] | null>;
  actionsEffectuees: FormControl<IDegradation['actionsEffectuees'] | null>;
  dateDetection: FormControl<IDegradation['dateDetection'] | null>;
  dateLimite: FormControl<IDegradation['dateLimite'] | null>;
  commentaire: FormControl<IDegradation['commentaire'] | null>;
  nextStep: FormControl<IDegradation['nextStep'] | null>;
  ticketOceane: FormControl<IDegradation['ticketOceane'] | null>;
  site: FormControl<IDegradation['site'] | null>;
};

export type DegradationFormGroup = FormGroup<DegradationFormGroupContent>;

/**
 * Retourne un timestamp pour un Dayjs ou Date ou null
 */
function getTimestamp(date?: Dayjs | Date | null): number {
  if (!date) return 0;
  if (typeof (date as Dayjs).valueOf === 'function') {
    return (date as Dayjs).valueOf();
  }
  if (date instanceof Date) {
    return date.getTime();
  }
  return 0;
}

/**
 * Retourne le délai actif (non TERMINÉ) le plus récent ou null
 */
export function getActiveDelai(degradation: IDegradation): IDelaiIntervention | null {
  if (!degradation.delais || degradation.delais.length === 0) return null;

  return (
    degradation.delais
      .filter(d => d.statut?.toUpperCase() !== 'TERMINE')
      .sort((a, b) => getTimestamp(b.dateDebut) - getTimestamp(a.dateDebut))[0] ?? null
  );
}

export { IDelaiIntervention };
