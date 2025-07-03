import { FormControl, FormGroup } from '@angular/forms';

/**
 * Interfaces associées aux entités liées
 */
export interface IUtilisateur {
  id: number;
  nom?: string;
}

export interface ISite {
  id: number;
  nom?: string;
  latitude?: number;
  longitude?: number;
}

/**
 * Interface principale Degradation
 */
export interface IDegradation {
  id: number;
  numero: string;
  localite?: string | null;
  contactTemoin?: string | null;
  typeAnomalie?: string | null;
  priorite?: string | null;
  problem?: string | null;
  porteur?: string | null;
  statut?: string | null;
  actionsEffectuees?: string | null;
  dateDetection?: Date | null;
  commentaire?: string | null;
  utilisateur?: IUtilisateur | null;
  site?: ISite | null;
}

/**
 * Nouveau modèle avec id null
 */
export type NewDegradation = Omit<IDegradation, 'id'> & { id: null };

/**
 * Types de formulaire
 */
export type DegradationFormGroupInput = IDegradation | Partial<NewDegradation>;

export type DegradationFormGroupContent = {
  id: FormControl<IDegradation['id'] | null>;
  numero: FormControl<IDegradation['numero']>;
  localite: FormControl<IDegradation['localite']>;
  contactTemoin: FormControl<IDegradation['contactTemoin']>;
  typeAnomalie: FormControl<IDegradation['typeAnomalie']>;
  priorite: FormControl<IDegradation['priorite']>;
  problem: FormControl<IDegradation['problem']>;
  porteur: FormControl<IDegradation['porteur']>;
  statut: FormControl<IDegradation['statut']>;
  actionsEffectuees: FormControl<IDegradation['actionsEffectuees']>;
  dateDetection: FormControl<IDegradation['dateDetection']>;
  commentaire: FormControl<IDegradation['commentaire']>;
  utilisateur: FormControl<IDegradation['utilisateur']>;
  site: FormControl<IDegradation['site']>;
};

export type DegradationFormGroup = FormGroup<DegradationFormGroupContent>;
