import { IUtilisateur, NewUtilisateur } from './utilisateur.model';

export const sampleWithRequiredData: IUtilisateur = {
  id: 7635,
};

export const sampleWithPartialData: IUtilisateur = {
  id: 12643,
};

export const sampleWithFullData: IUtilisateur = {
  id: 18250,
  nom: 'si bien que concernant',
  prenom: 'résister',
  email: 'Yvonne.Philippe@hotmail.fr',
  motDePasse: 'groin groin',
};

export const sampleWithNewData: NewUtilisateur = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
