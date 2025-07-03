import dayjs from 'dayjs/esm';

export interface ISite {
  id: number;
  nomSite?: string | null;
  codeOCI?: string | null;
  longitude?: number | null;
  latitude?: number | null;
  gestionnaire?: string | null;
  proprietaire?: string | null;
  equipementier?: string | null;
  typeSite?: string | null;
  modeAntenne?: string | null;
  statut?: string | null;
  couche?: string | null;
  enService?: boolean | null;
  technologie?: string | null;
  dateMiseEnService?: dayjs.Dayjs | null;
  dateMes2G?: dayjs.Dayjs | null;
  dateMes3G?: dayjs.Dayjs | null;
}

export type NewSite = Omit<ISite, 'id'> & { id: null };
