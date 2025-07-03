import dayjs from 'dayjs/esm';

import { ISite, NewSite } from './site.model';

export const sampleWithRequiredData: ISite = {
  id: 15724,
};

export const sampleWithPartialData: ISite = {
  id: 28694,
  codeOCI: "à l'instar de",
  gestionnaire: 'rédaction',
  equipementier: 'amorphe',
  modeAntenne: 'différencier à bas de',
  statut: 'administration',
  enService: false,
  technologie: 'insipide exploiter gens',
};

export const sampleWithFullData: ISite = {
  id: 31836,
  nomSite: 'cyan',
  codeOCI: 'commissionnaire trop',
  longitude: 20675.7,
  latitude: 23458.23,
  gestionnaire: 'membre de l’équipe alors que',
  proprietaire: 'actionnaire',
  equipementier: 'parlementaire comme',
  typeSite: 'délectable',
  modeAntenne: 'foule',
  statut: 'bientôt doucement juriste',
  couche: 'rédiger',
  enService: false,
  technologie: 'de crainte que',
  dateMiseEnService: dayjs('2025-06-20'),
  dateMes2G: dayjs('2025-06-20'),
  dateMes3G: dayjs('2025-06-19'),
};

export const sampleWithNewData: NewSite = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
