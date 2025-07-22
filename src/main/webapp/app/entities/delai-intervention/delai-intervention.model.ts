import dayjs from 'dayjs';

export interface IDelaiIntervention {
  id?: number;
  dateDebut?: dayjs.Dayjs | null;
  dateLimite?: dayjs.Dayjs | null;
  commentaire?: string;
  statut?: StatutDelaiIntervention;
  acteur?: string | null;
  degradation?: any | null; // ID ou objet IDegradation
}

export interface NewDelaiIntervention extends Omit<IDelaiIntervention, 'id'> {
  id: null;
}

export enum StatutDelaiIntervention {
  EN_COURS = 'EN_COURS',
  TERMINE = 'TERMINE',
  ANNULE = 'ANNULE',
  CLOTURE = 'CLOTURE',
  EN_ATTENTE = 'EN_ATTENTE',
}
