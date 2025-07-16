import dayjs from 'dayjs';
import { IUtilisateur } from '../utilisateur/utilisateur.model';

/**
 * Enumération du statut d’un délai
 */
export enum StatutDelaiIntervention {
  EN_COURS = 'EN_COURS',
  TERMINE = 'TERMINE',
  ANNULE = 'ANNULE',
}

/**
 * Interface principale
 */
export interface IDelaiIntervention {
  id: number;
  dateDebut?: dayjs.Dayjs | null;
  dateLimite?: dayjs.Dayjs | null;
  commentaire?: string | null;
  statut?: StatutDelaiIntervention | null;
  acteur?: IUtilisateur | null;
}

/**
 * Interface pour la création d’un nouveau délai (id = null)
 */
export type NewDelaiIntervention = Omit<IDelaiIntervention, 'id'> & { id: null };
