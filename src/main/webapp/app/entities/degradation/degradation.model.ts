import { ISite } from 'app/entities/site/site.model';
import { FormControl, FormGroup } from '@angular/forms';

/**
 * Interface principale Degradation
 */
export interface IDegradation {
  id: number; // <-- Ajoute cette ligne obligatoire
  localite?: string | null;
  contactTemoin?: string | null;
  typeAnomalie?: string | null;
  priorite?: string | null;
  porteur?: string | null;
  porteur2?: string | null;
  statut?: string | null;
  actionsEffectuees?: string | null;
  dateDetection?: Date | null;
  dateLimite?: string; // <== Ajoute cette ligne
  commentaire?: string | null;
  nextStep?: string | null;
  ticketOceane?: string | null;

  site?: ISite | null;
}

/**
 * Nouveau modèle utilisé pour les créations (id = null)
 */
export type NewDegradation = Omit<IDegradation, 'id'> & { id: null };

/**
 * Utilisé comme type d'entrée du formulaire
 */
export type DegradationFormGroupInput = IDegradation | Partial<NewDegradation>;

/**
 * Structure du FormGroup Angular avec les bons types
 */
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
  commentaire: FormControl<IDegradation['commentaire'] | null>;
  nextStep: FormControl<IDegradation['nextStep'] | null>;
  ticketOceane: FormControl<IDegradation['ticketOceane'] | null>;

  site: FormControl<IDegradation['site'] | null>;
};

/**
 * FormGroup typé pour Degradation
 */
export type DegradationFormGroup = FormGroup<DegradationFormGroupContent>;
