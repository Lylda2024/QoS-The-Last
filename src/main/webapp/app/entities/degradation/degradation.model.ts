import { Dayjs } from 'dayjs';
import { ISite } from 'app/entities/site/site.model';
import { IDelaiIntervention } from 'app/entities/delai-intervention/delai-intervention.model';

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
  dateLimite?: Dayjs | null; // <-- ajouté ici
  commentaire?: string | null;
  nextStep?: string | null;
  ticketOceane?: string | null;
  site?: ISite | null;
  delais?: IDelaiIntervention[] | null;
  nom?: string;
  description?: string;
}

// Pour les nouveaux objets (création)
export type NewDegradation = Omit<IDegradation, 'id'> & { id: null };

// Utilisé pour initialiser ou remettre à zéro le formulaire
export type DegradationFormGroupInput = IDegradation | Partial<NewDegradation>;

// Fonction utilitaire pour récupérer le délai actif
export function getActiveDelai(degradation: IDegradation): IDelaiIntervention | null {
  if (!degradation.delais || degradation.delais.length === 0) return null;
  return (
    degradation.delais
      .filter(d => d.statut !== 'TERMINE')
      .sort((a, b) => (b.dateDebut?.toDate?.().getTime?.() ?? 0) - (a.dateDebut?.toDate?.().getTime?.() ?? 0))[0] ?? null
  );
}
