import dayjs from 'dayjs/esm';

import { IDegradation } from 'app/entities/degradation/degradation.model';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';

export enum StatutDelai {
  EN_COURS = 'EN_COURS',
  TRANSFERE = 'TRANSFERE',
  TERMINE = 'TERMINE',
}

export interface IDelaiIntervention {
  id?: number;
  dateDebut?: string; // ISO string format (ex: "2025-07-14T10:00:00Z")
  dateLimite?: string;
  commentaire?: string;
  statut?: StatutDelai;
  degradation?: IDegradation | null;
  acteur?: IUtilisateur | null; // Note: utilisé acteur au lieu de utilisateur selon ton exemple
}

export type NewDelaiIntervention = Omit<IDelaiIntervention, 'id'> & { id: null };

export const sampleWithRequiredData: IDelaiIntervention = {
  id: 8473,
};

export const sampleWithPartialData: IDelaiIntervention = {
  id: 5278,
  commentaire: 'Exemple de commentaire partiel',
};

export const sampleWithFullData: IDelaiIntervention = {
  id: 13308,
  acteur: { id: 1, nom: 'Dupont', prenom: 'Jean' }, // exemple objet Utilisateur
  commentaire: 'Délai bien respecté',
  dateDebut: dayjs('2025-06-20T09:52').toISOString(),
  dateLimite: dayjs('2025-06-25T17:00').toISOString(),
  statut: StatutDelai.EN_COURS,
  degradation: { id: 101 }, // exemple objet Degradation minimal
};

export const sampleWithNewData: NewDelaiIntervention = {
  id: null,
  statut: StatutDelai.EN_COURS,
  // autres champs optionnels initialisés ici si besoin
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
