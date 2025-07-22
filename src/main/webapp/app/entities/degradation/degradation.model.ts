import { ISite } from 'app/entities/site/site.model';
import { IDelaiIntervention } from 'app/entities/delai-intervention/delai-intervention.model';
import { FormControl, FormGroup } from '@angular/forms';

/**
 * Interface principale Degradation
 */
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
  dateDetection?: string | null;
  dateLimite?: string | null; // ✅ ajouté
  commentaire?: string | null;
  nextStep?: string | null;
  ticketOceane?: string | null;

  site?: ISite | null;

  /** Relation en lecture seule, NON incluse dans le formulaire */
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
  dateLimite: FormControl<IDegradation['dateLimite'] | null>; // ✅ ajouté
  commentaire: FormControl<IDegradation['commentaire'] | null>;
  nextStep: FormControl<IDegradation['nextStep'] | null>;
  ticketOceane: FormControl<IDegradation['ticketOceane'] | null>;
  site: FormControl<IDegradation['site'] | null>;
};

export type DegradationFormGroup = FormGroup<DegradationFormGroupContent>;
