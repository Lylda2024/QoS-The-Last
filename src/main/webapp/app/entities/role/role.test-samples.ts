import { IRole, NewRole } from './role.model';

export const sampleWithRequiredData: IRole = {
  id: 2153,
};

export const sampleWithPartialData: IRole = {
  id: 18953,
  nom: 'hors splendide',
};

export const sampleWithFullData: IRole = {
  id: 16604,
  nom: 'de manière à ce que au-devant terminer',
  description: 'à cause de',
};

export const sampleWithNewData: NewRole = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
