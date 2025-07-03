import dayjs from 'dayjs/esm';

import { IHistorique, NewHistorique } from './historique.model';

export const sampleWithRequiredData: IHistorique = {
  id: 8473,
};

export const sampleWithPartialData: IHistorique = {
  id: 5278,
};

export const sampleWithFullData: IHistorique = {
  id: 13308,
  utilisateur: 'de la part de',
  section: 'fonctionnaire vibrer pis',
  horodatage: dayjs('2025-06-20T09:52'),
};

export const sampleWithNewData: NewHistorique = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
