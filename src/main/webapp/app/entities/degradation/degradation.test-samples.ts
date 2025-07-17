import { IDegradation, NewDegradation } from './degradation.model';

export const sampleWithRequiredData: IDegradation = {
  id: 10752,
};

export const sampleWithPartialData: IDegradation = {
  id: 12770,

  contactTemoin: 'en decà de glouglou',
  porteur: 'gestionnaire',
};

export const sampleWithFullData: IDegradation = {
  id: 20978,

  localite: 'agréable',
  contactTemoin: 'détester',
  typeAnomalie: 'clac atchoum',
  priorite: 'bof',
  problem: 'du fait que assez',
  porteur: 'oser',
  actionsEffectuees: 'aggraver',
};

export const sampleWithNewData: NewDegradation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
