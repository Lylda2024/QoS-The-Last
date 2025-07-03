import { ITypeUtilisateur, NewTypeUtilisateur } from './type-utilisateur.model';

export const sampleWithRequiredData: ITypeUtilisateur = {
  id: 19001,
};

export const sampleWithPartialData: ITypeUtilisateur = {
  id: 17434,
  description: 'camarade',
  permissions: 'ding toc-toc à seule fin de',
};

export const sampleWithFullData: ITypeUtilisateur = {
  id: 11606,
  nom: 'insolite',
  description: 'presque turquoise afin que',
  niveau: 26299,
  permissions: 'circulaire rectangulaire demain',
};

export const sampleWithNewData: NewTypeUtilisateur = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
